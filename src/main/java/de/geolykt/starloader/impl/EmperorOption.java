package de.geolykt.starloader.impl;

import java.util.Objects;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.people.DynastyMember;

import snoddasmannen.galimulator.EmploymentAgency;
import snoddasmannen.galimulator.Job;
import snoddasmannen.galimulator.Person;
import snoddasmannen.galimulator.ui.class_42;

/**
 * A line entry in the dialog that allows users to choose their new emperor.
 */
public class EmperorOption extends class_42 {

    protected final EmploymentAgency agency;
    protected final Job job;
    protected final DynastyMember person;

    public EmperorOption(EmploymentAgency parentClass, DynastyMember person, Job b, boolean c) {
        super((Person) Objects.requireNonNull(person, "The person may not be null!"), b, c);
        this.person = person;
        this.job = b;
        this.agency = parentClass;
    }

    @Override
    public void b() {
        this.agency.a(this.job, (Person) this.person);
        Galimulator.setPaused(false);
    }
}
