package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.people.EmperorDeathEvent;
import de.geolykt.starloader.api.gui.BasicDialog;
import de.geolykt.starloader.api.gui.BasicDialogBuilder;
import de.geolykt.starloader.impl.EmperorOption;

import snoddasmannen.galimulator.Claim;
import snoddasmannen.galimulator.EmploymentAgency;
import snoddasmannen.galimulator.Job;
import snoddasmannen.galimulator.Job.JobType;
import snoddasmannen.galimulator.Person;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.FlowLayout.FlowDirection;

@Mixin(EmploymentAgency.class)
public class EmploymentAgencyMixins {

    /**
     * The description of the dialog that is created when the player's emperor died and the player is able to choose
     * a successor.
     */
    @NotNull
    private static transient String emperorDeadSuccessorDesc = "Your dearest leader has passed on, oh no! It is time to select a new one from the list of top claimants to the throne.";

    /**
     * The description of the dialog button that is created when the player's emperor died and the player is able to choose a successor.
     */
    @NotNull
    private static transient String emperorDeadSuccessorKey = "OK, no problems, I'll find a good replacement";

    @Shadow
    transient ExecutorService b;

    @Unique
    @Nullable
    private transient BasicDialog selectEmperorDialog;

    @Overwrite
    private Person a(final Job job, final int n) {
        List<Person> potentialCandidates;
        if (job.getType() == JobType.EMPEROR && (job.getEmployer() == Galimulator.getUniverse().getNeutralEmpire() || job.getPreviousHolder() != null)) {
            potentialCandidates = job.n();
            if (potentialCandidates.isEmpty()) {
                potentialCandidates = this.b(n);
            }
        } else {
            potentialCandidates = this.b(n);
        }
        if (potentialCandidates.isEmpty()) {
            return null;
        }
        Person.f = 0;
        if (job.getType() == JobType.EMPEROR) {
            if (Galimulator.getUniverse().getPlayerEmpire() == job.getEmployer().getJobEmpire()) {
                // Player empire
                potentialCandidates.removeIf(person -> person.a(job) <= 0 || Claim.b(person, job) == null);
                if (!potentialCandidates.isEmpty()) {
                    List<@NotNull DynastyMember> candidates = new ArrayList<>(potentialCandidates.size());
                    for (Person o : potentialCandidates) {
                        if (o == null) {
                            continue;
                        }
                        candidates.add((DynastyMember) o);
                    }
                    @SuppressWarnings("deprecation")
                    EmperorDeathEvent evt = new de.geolykt.starloader.api.event.people.PlayerEmperorDeathEvent(candidates);
                    EventManager.handleEvent(evt);
                    List<@NotNull DynastyMember> finalSuggestions = evt.getSuccessors();
                    if (finalSuggestions.isEmpty()) {
                        for (Person o : potentialCandidates) {
                            if (o == null) {
                                continue;
                            }
                            finalSuggestions.add((DynastyMember) o);
                        }
                    }
                    if (finalSuggestions.size() == 1) {
                        new EmperorOption((EmploymentAgency) (Object) this, finalSuggestions.get(0), job, true).b();
                        return null;
                    }
                    Galimulator.setPaused(true);
                    BasicDialogBuilder dialogBuilder = new BasicDialogBuilder("Select new emperor", emperorDeadSuccessorDesc, Arrays.asList(emperorDeadSuccessorKey));
                    dialogBuilder.addCloseListener((cause, text) -> {
                        if (emperorDeadSuccessorKey.equals(text)) {
                            Vector<EmperorOption> options = new Vector<>();
                            boolean firstEntry = true;
                            for (DynastyMember person : finalSuggestions) {
                                options.add(new EmperorOption((EmploymentAgency) (Object) this, person, job, firstEntry));
                                firstEntry = false;
                            }
                            if (!firstEntry) {
                                Space.a(options, FlowDirection.VERTICAL, null);
                            }
                        }
                    });
                    Galimulator.runTaskOnNextFrame(() -> {
                        BasicDialog dialog = selectEmperorDialog;
                        if (dialog != null && !dialog.isClosed()) {
                            dialog.close();
                        }
                        selectEmperorDialog = dialogBuilder.buildAndShowNow();
                    });
                    return null;
                }
                new BasicDialogBuilder("Emperor dead", "Bad news, the emperor kind of died! There weren't really any great options out there, so we just picked somebody off the street").show();
            } else {
                // Non-player empire
                List<@NotNull DynastyMember> candidates = new ArrayList<>(potentialCandidates.size());
                for (Person o : potentialCandidates) {
                    if (o == null) {
                        continue;
                    }
                    candidates.add((DynastyMember) o);
                }
                EmperorDeathEvent evt = new EmperorDeathEvent(candidates, NullUtils.requireNotNull((Empire) job.getEmployer()));
                EventManager.handleEvent(evt);
                List<@NotNull DynastyMember> finalSuggestions = evt.getSuccessors();
                if (finalSuggestions.size() == 1) {
                    return (Person) finalSuggestions.get(0);
                }
                potentialCandidates.clear();
                for (DynastyMember member : finalSuggestions) {
                    potentialCandidates.add((Person) member);
                }
            }
        }
        final ArrayList<Callable<EmploymentAgency.Local2>> callbacks = new ArrayList<>();
        final int n2 = potentialCandidates.size() / 100 + 1;
        for (int i = 0; i < n2; ++i) {
            @SuppressWarnings("unchecked")
            Callable<EmploymentAgency.Local2> callable = ((EmploymentAgency) (Object) this).new Local1(i * 100, potentialCandidates, job);
            callbacks.add(callable);
        }
        Person successor = null;
        int successorMerit = 0;
        try {
            if (this.b == null) {
                this.b = Executors.newFixedThreadPool(4);
            }
            for (Future<EmploymentAgency.Local2> finishedTask : b.invokeAll(callbacks)) {
                final EmploymentAgency.Local2 finishedTaskReturn = finishedTask.get();
                if (successor == null || finishedTaskReturn.b > successorMerit) {
                    successor = finishedTaskReturn.a;
                    successorMerit = finishedTaskReturn.b;
                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return successor;
    }

    @Shadow
    private List<Person> b(@SuppressWarnings("unused") final int n) {
        return null;
    }
}
