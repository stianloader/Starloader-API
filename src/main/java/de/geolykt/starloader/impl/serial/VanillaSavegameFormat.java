package de.geolykt.starloader.impl.serial;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.DeviceConfiguration;
import snoddasmannen.galimulator.EmploymentAgency;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.SpaceState;
import snoddasmannen.galimulator.guides.class_0;

public class VanillaSavegameFormat implements SavegameFormat {

    @NotNull
    public static final VanillaSavegameFormat INSTANCE = new VanillaSavegameFormat();

    // TODO move to the unsafe class
    @SuppressWarnings("deprecation")
    static synchronized void inferSavegameData() {

        Galimulator.GameImplementation galiImpl = Galimulator.getImplementation();
        Galimulator.Unsafe unsafe = galiImpl.getUnsafe();

        // Many magic methods and stuff. See Space#i(String) (as of galimulator-4.9-STABLE)
        Space.setBackgroundTaskDescription("Loading galaxy: Regenerating regions");

        HashMap<Integer, Star> uidToStar = new HashMap<>();
        HashMap<Integer, ActiveEmpire> uidToEmpire = new HashMap<>();

        for (Star star : unsafe.getStarsUnsafe()) {
            star.setInternalRandom(new Random());
            uidToStar.put(star.getUID(), star);
        }

        for (ActiveEmpire empire : unsafe.getEmpiresUnsafe()) {
            empire.setRecentlyLostStars(new ArrayDeque<>());
            empire.setInternalRandom(new Random());
            uidToEmpire.put(empire.getUID(), empire);
        }

        galiImpl.getNeutralEmpire().setInternalRandom(new Random());
        galiImpl.getNeutralEmpire().setRecentlyLostStars(new ArrayDeque<>());
        @SuppressWarnings("null")
        @NotNull
        final NamespacedKey nullReligion = NullUtils.provideNull();
        galiImpl.getNeutralEmpire().setReligion(nullReligion);
        Space.au(); // probably sets up the background effects. Accesses the LET_IT_SNOW setting as well as creating AmbientStarEffect among others
        Space.getMapData().getGenerator().prepareGenerator(); // Change the xmax and ymax of the generator area
        Space.regenerateVoronoiCells(); // big calculations with voronoi diagrams
        Space.setBackgroundTaskDescription("Loading galaxy: Reconstructing map metadata");
        Space.maxXCache = Space.getMaxX(); // set the width/height of the board
        Space.maxYCache = Space.getMaxY();

        // repopulate the starlanes (this was extracted from another method)
        // Also sets the owner empire, which was also extracted from another method
        for (Star star : unsafe.getStarsUnsafe()) {
            Vector<Star> neighbours = new Vector<>();
            for (Integer starB : star.getNeighbourIDs()) {
                neighbours.add(uidToStar.get(starB));
            }
            star.setNeighbours(neighbours);
            ActiveEmpire owner = uidToEmpire.get(star.getAssignedEmpireUID());
            if (owner == null) {
                owner = galiImpl.getNeutralEmpire();
            }
            star.setAssignedEmpire(owner);
        }

        Space.ap(); // setup quad trees
        if (unsafe.getAlliancesUnsafe() == null) {
            unsafe.setAlliancesUnsafe(new Vector<>());
        } else {
            for (Alliance alliance : unsafe.getAlliancesUnsafe()) {
                for (ActiveEmpire member : alliance.getMembers()) {
                    member.setAlliance(alliance);
                }
            }
        }

        Vector<DynastyMember> followedMembers = new Vector<>();
        for (DynastyMember member : unsafe.getPeopleUnsafe()) {
            if (member.isFollowed()) {
                followedMembers.add(member);
            }
        }
        unsafe.setFollowedPeopleUnsafe(followedMembers);
        class_0.b();

        Space.getMapData().getGenerator().onLoad();
        GalFX.m.zoom = GalFX.e();
        GalFX.m.update();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    static synchronized void loadVanillaState(@NotNull InputStream is) throws IOException {

        Object readObject;
        try (ObjectInputStream in = new ObjectInputStream(is)) {
            try {
                readObject = in.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new IOException("Failed to read savegame.", e);
            }
            if (!(readObject instanceof SpaceState)) {
                throw new IOException("The read object was not the excepted obect.");
            }
        }
        SpaceState spaceState = (SpaceState) readObject;
        Space.history = spaceState.history;

        GalimulatorImplementation galiImpl = (GalimulatorImplementation) Galimulator.getImplementation();
        Space.setBackgroundTaskDescription("Loading galaxy: Importing data");
        galiImpl.setMap((Map) NullUtils.requireNotNull(spaceState.mapData));
        galiImpl.setGameYear(spaceState.milliYear);
        galiImpl.setNeutralEmpire(NullUtils.requireNotNull((ActiveEmpire) spaceState.neutralEmpire));
        galiImpl.setPlayer(spaceState.player);
        galiImpl.setUsedSandbox(spaceState.sandboxUsed);
        galiImpl.setTranscendedEmpires(spaceState.transcended);
        galiImpl.setWarsUnsafe((Vector) spaceState.wars);
        galiImpl.setVanityHolder(spaceState.vanity);
        galiImpl.setActorsUnsafe(NullUtils.requireNotNull((Vector) spaceState.actors));
        galiImpl.setAlliancesUnsafe(NullUtils.requireNotNull((Vector) spaceState.alliances));
        galiImpl.setArtifactsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.artifacts));
        galiImpl.setCorporationsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.corporations));
        galiImpl.setDisruptedStarsUnsafe(NullUtils.requireNotNull((Vector<Star>) spaceState.disruptedStars));
        galiImpl.setEmpiresUnsafe(NullUtils.requireNotNull((Vector) spaceState.empires));
        galiImpl.setPeopleUnsafe(NullUtils.requireNotNull((Vector) spaceState.persons));
        galiImpl.setQuestsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.quests));
        galiImpl.setStarsUnsafe(NullUtils.requireNotNull((Vector) spaceState.stars));
        EmploymentAgency.setInstance(spaceState.employmentAgency);
        Space.p = null;
        Space.E = true;
        Space.setBackgroundTaskDescription(null);
    }

    static synchronized void saveVanillaState(@NotNull OutputStream raw) throws Throwable {
        Space.J = 0; // reset Stack depth
        GalimulatorImplementation galiImpl = (GalimulatorImplementation) Galimulator.getImplementation();
        SpaceState var2 = galiImpl.createState();
        if (DeviceConfiguration.getConfiguration().useXStream()) {
            LoggerFactory.getLogger(VanillaSavegameFormat.class).warn("XStream is not supported for saving at all.");
        }
        ObjectOutputStream var5 = new ObjectOutputStream(raw);
        var5.writeObject(var2);
        var5.flush();
    }

    private VanillaSavegameFormat() {
        // Prevent arbitrary initialisation of this class
    }

    @Override
    @NotNull
    public String getName() {
        return "Vanilla";
    }

    @Override
    public void loadGameState(byte[] data) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            loadGameState(in);
        }
    }

    @Override
    public synchronized void loadGameState(@NotNull InputStream input) throws IOException {
        EventManager.handleEvent(new GalaxyLoadingEvent());
        loadVanillaState(input);
        inferSavegameData();
        EventManager.handleEvent(new GalaxyLoadingEndEvent(this, new WriteableMetadataState())); // TODO perhaps make a NOP metadata collection?
    }

    @Override
    public void saveGameState(@NotNull OutputStream out, @Nullable String reason, @Nullable String location) throws IOException, OutOfMemoryError {
        if (reason == null) {
            reason = "Programmer issued save";
        }
        if (location == null) {
            location = "Unespecified";
        }
        EventManager.handleEvent(new GalaxySavingEvent(reason, location, new BasicMetadataCollector())); // TODO perhaps make a NOP metadata collection?

        GalaxySavingEndEvent saveEndEvent = new GalaxySavingEndEvent(location);
        try {
            VanillaSavegameFormat.saveVanillaState(out);
        } catch (Throwable var6) {
            if (var6 instanceof ThreadDeath) {
                throw (ThreadDeath) var6;
            }
            if (var6 instanceof OutOfMemoryError) {
                throw (OutOfMemoryError) var6;
            }
            throw new IOException("Issue during serialisation.", var6);
        } finally {
            EventManager.handleEvent(saveEndEvent);
        }
    }

    @Override
    public boolean supportsSLAPIMetadata() {
        return false;
    }
}
