package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;

/**
 * Object that defines a war between two parties. Right now these parties are empires, however this
 * may change in the future with the inclusion of vassals.
 */
public interface War extends Dateable {

    /**
     * Obtains the amount of time that has passed since the war was started relative to the time in which
     * the last star was taken.
     *
     * @see #getStartDate()
     * @see #getDateOfLastAction()
     */
    @Override
    public default int getAge() {
        return getDateOfLastAction() - getStartDate();
    }

    /**
     * Obtains the date / year in which the last star was taken. This does not mean that the war ended.
     *
     * @return The date in which the latest taken star was taken
     */
    public int getDateOfLastAction();

    /**
     * Obtains the amount of destroyed ships.
     * It is not known whether this value is used nor whether it can be considered accurate,
     * what is known however is that there is a field that appears to do exactly that.
     *
     * @return The amount of destroyed ships.
     */
    public int getDestroyedShips();

    /**
     * Obtains one participant of the war.
     * Multiple invocations to this method should return the same empire.
     *
     * @return A participant of the war
     */
    public @NotNull Empire getEmpireA();

    /**
     * Obtains another participant of the war.
     * Multiple invocations to this method should return the same empire.
     *
     * @return A participant of the war
     */
    public @NotNull Empire getEmpireB();

    /**
     * Obtains the date / year in which the war was started.
     * This method is an alias for {@link #getStartDate()}.
     *
     * @return The date the war was started
     */
    @Override
    public default int getFoundationYear() {
        return getStartDate();
    }

    /**
     * Obtains the amount of stars that {@link #getEmpireA()} has won in the war
     * subtracted with the stars that it has lost.
     *
     * @return The delta of the stars won and the stars lost.
     */
    public int getStarDelta();

    /**
     * Obtains the date / year in which the war was started.
     *
     * @return The date the war was started
     */
    public int getStartDate();

    /**
     * Increments the ship destruction counter by one.
     */
    public void noteShipDestruction();

    /**
     * Notes that an empire has taken a star.
     *
     * @param empire The empire that took a star from another empire
     * @throws IllegalArgumentException If the given empire does not participate in the war
     */
    public void noteStarChange(@NotNull Empire empire) throws IllegalArgumentException;

    /**
     * Sets the amount of ships that were destroyed in the war.
     * It is mostly unknown what this value is used for.
     *
     * @param count The count
     */
    public void setDestroyedShips(int count);

    /**
     * Sets the amount of stars that {@link #getEmpireA()} has won in the war
     * subtracted with the stars that it has lost.
     *
     * @param count The delta of the stars won and the stars lost.
     */
    public void setStarDelta(int count);
}
