package de.geolykt.starloader.api.player;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import de.geolykt.starloader.DeprecatedSince;

import snoddasmannen.galimulator.diplomacy.PlayerRequest;

/**
 * @deprecated This will be phased out in favour of a proper registry API
 *
 * Wrapper container class for diplomatic requests.
 */
@ScheduledForRemoval
@DeprecatedSince("1.2.0")
@Deprecated
// TODO do it!
// FIXME OUTDATED!
public final class DiplomacyRequests {

    public static final DiplomacyRequest SAY_HI = (DiplomacyRequest) PlayerRequest.SAY_HI;
    public static final DiplomacyRequest MAKE_PEACE = (DiplomacyRequest) PlayerRequest.MAKE_PEACE;
    public static final DiplomacyRequest DECLARE_WAR = (DiplomacyRequest) PlayerRequest.DECLARE_WAR;
    public static final DiplomacyRequest LEAVE_ALLIANCE = (DiplomacyRequest) PlayerRequest.LEAVE_ALLIANCE;
    public static final DiplomacyRequest START_NEW_ALLIANCE = (DiplomacyRequest) PlayerRequest.START_NEW_ALLIANCE;
    public static final DiplomacyRequest INVITE_TO_MY_ALLIANCE = (DiplomacyRequest) PlayerRequest.INVITE_TO_MY_ALLIANCE;
    public static final DiplomacyRequest JOIN_EXISTING_ALLIANCE = (DiplomacyRequest) PlayerRequest.JOIN_EXISTING_ALLIANCE;
}
