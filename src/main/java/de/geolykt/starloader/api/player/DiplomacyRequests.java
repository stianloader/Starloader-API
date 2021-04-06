package de.geolykt.starloader.api.player;

import snoddasmannen.galimulator.diplomacy.PlayerRequest;

/**
 * @deprecated This will be phased out by a proper registry API
 *
 * Wrapper container class for diplomatic requests.
 */
@Deprecated(forRemoval = true, since = "1.1.1")
public final class DiplomacyRequests {

    public static final DiplomacyRequest SAY_HI = (DiplomacyRequest) PlayerRequest.a;
    public static final DiplomacyRequest MAKE_PEACE = (DiplomacyRequest) PlayerRequest.b;
    public static final DiplomacyRequest DECLARE_WAR = (DiplomacyRequest) PlayerRequest.c;
    public static final DiplomacyRequest LEAVE_ALLIANCE = (DiplomacyRequest) PlayerRequest.d;
    public static final DiplomacyRequest START_NEW_ALLIANCE = (DiplomacyRequest) PlayerRequest.e;
    public static final DiplomacyRequest INVITE_TO_MY_ALLIANCE = (DiplomacyRequest) PlayerRequest.f;
    public static final DiplomacyRequest JOIN_EXISTING_ALLIANCE = (DiplomacyRequest) PlayerRequest.g;
}
