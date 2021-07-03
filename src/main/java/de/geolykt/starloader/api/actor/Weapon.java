package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

/**
 * A weapon. Please note that other than with the {@link WeaponType} class, the weapon is not a singleton
 * and there will be at least one instance of this class per occourence on the game board.
 */
public interface Weapon {

    /**
     * A functional interface that behaves like a listener.
     * It is called whenever {@link Weapon#fireAt(float, float)} or it's base implementation is called.
     */
    @FunctionalInterface
    public static interface WeaponListener {

        /**
         * The method that is called whenever the weapon fired at something (or nothing).
         *
         * @param x The X-position of where the shots have been fired
         * @param y The Y-position of where the shots have been fired
         */
        public void onFire(float x, float y);
    }

    /**
     * Registers a listener.
     *
     * @param listener The listener to register
     */
    public void addListener(@NotNull WeaponListener listener);

    /**
     * Obtains whether this weapon can shoot anything at this time.
     * It is only true if {@link #isDisabled()} is false and if {@link #getCooldown()} is 0.
     *
     * @return True if the weapon can fire
     */
    public boolean canShoot();

    /**
     * Fires the weapon at a given position.
     * This method does NOT check whether the weapon may actually fire, so this should be instead done by
     * the caller, however afterwards the cooldown is set to the base cooldown of the weapon.
     *
     * @param x The X position of the target
     * @param y The Y position of the target
     * @see #canShoot()
     */
    public void fireAt(float x, float y);

    /**
     * Obtains the cooldown that is applied on the weapon whenever it fires.
     *
     * @return The base cooldown.
     */
    public int getBaseCooldown();

    /**
     * Obtains the cooldown of the weapon. If is decreased everytime {@link #tickCooldown()} is called
     * and can at minimum reach 0. If it is a non-0 value, then the weapon may not fire right now.
     *
     * @return The cooldown of the weapon.
     */
    public int getCooldown();

    /**
     * Obtains the user-friendly name of the weapon.
     *
     * @return The name of the weapon
     */
    public @NotNull String getName();

    /**
     * Checks whether this weapon is (temporarily) disabled.
     *
     * @return The disabled/blocked modifier
     */
    public boolean isDisabled();

    /**
     * Sets the disabled modifier of this weapon. If it is true, then the weapon may not fire.
     *
     * @param disabled The new disabled modifier
     * @see #isDisabled()
     * @see #canShoot()
     */
    public void setDisabled(boolean disabled);

    /**
     * Method that is called once a tick to reduce the cooldown of the weapon - it applicable.
     */
    public void tickCooldown();
}
