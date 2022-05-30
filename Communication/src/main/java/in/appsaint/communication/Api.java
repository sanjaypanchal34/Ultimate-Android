package in.appsaint.communication;

public enum Api {

    // LIVE URL=============
    HOST("https://ultimateplayerhq.com/", 0),
    C_HOST("https://coaches-office.ultimateplayerhq.com/", 18),
    API_KEY_CHAT("3806183150b1226ee75f",-1),
    CHAT_SECRET("e2180cd2bd2899388783",-1),
    CHAT_CLUSTER("eu",-1),
    //================

    // Stagging URL=============
//    HOST("https://62a385bb-139e-4fcb-8af5-7596b278eb95.h3.conves.io/", 0),
//    C_HOST("http://161.35.41.64:9127/", 18),
//    API_KEY_CHAT("2568359a67b232bd2a79",-1),
//    CHAT_SECRET("2d29063cfb180aa1cc18",-1),
//    CHAT_CLUSTER("ap2",-1),
    //================

    BASE_URL(HOST + "/wp-json/ultimatehq-api/", 1),
    CHANNEL_NAME("private-ultimate-channel_",-1),
    EVENT_NAME("myMessages",-1),

    LOGIN(BASE_URL + "login", 2),
    FORGOT_PASSWORD(BASE_URL + "forget_password", 3),
    USER_PROFILE(BASE_URL + "get_user_profile_details", 4),
    HOME_SCREEN(BASE_URL + "home-screen", 5),
    UPDATE_PROFILE(BASE_URL + "update_user_profile_api", 5),
    UPDATE_EMAIL(BASE_URL + "update_user_profile_email", 6),
    CHANGE_PASSWORD(BASE_URL + "change_password", 7),
    CATEGORY_LIST(BASE_URL + "get_drill_category_list", 8),
    POST_BY_CATEGORY(BASE_URL + "get_drill_by_category", 9),
    POST_DETAIL(BASE_URL + "get_drill_details", 10),
    POST_COMMENTS(BASE_URL + "get_drill_comments", 11),
    ADD_COMMENT(BASE_URL + "add_drill_post_comment", 12),
    UPDATE_PROFILE_IMAGE(BASE_URL + "update_user_avatar", 13),
    LIKE_DISLIKE_POST(BASE_URL + "drill_likes", 14),
    GET_PRODUCT_LIST(BASE_URL + "get_products_list", 15),
    LOCKER_ROOM(BASE_URL + "favourite_drill_list", 16),
    SEARCH(BASE_URL + "search_session", 17),
    C_BASE_URL(C_HOST + "api/", 18),
    SQUAD(C_BASE_URL + "squad", 19),
    COACH(C_BASE_URL + "coach", 19),
    SQUAD_DELETE(C_BASE_URL + "squad/destroy/", 20),
    COACH_DELETE(C_BASE_URL + "coach/destroy/", 20),
    SQUAD_DETAIL(C_BASE_URL + "squad/get-squad-details/", 21),
    COACH_DETAIL(C_BASE_URL + "coach/get-coach-details/", 21),
    SQUAD_UPDATE(C_BASE_URL + "squad/update/", 22),
    COACH_UPDATE(C_BASE_URL + "coach/update/", 22),
    SQUAD_CREATE(C_BASE_URL + "squad/store", 23),
    COACH_CREATE(C_BASE_URL + "coach/store", 23),
    EVENTS(C_BASE_URL + "event/get-event", 23),
    EVENT_DATA_FROM_DATE(C_BASE_URL + "event/get-event-data", 23),
    VERIFY_TOKEN(C_BASE_URL + "verify-app-token", 23),
    TRAINING_DETAIL_FOR_EDIT(C_BASE_URL + "event/edit-training-event/", 23),
    TRAINING_DETAIL(C_BASE_URL + "event/view-training-session/", 23),
    EVENT_DETAIL_FOR_EDIT(C_BASE_URL + "event/edit-match-event/", 23),
    EVENT_DETAIL(C_BASE_URL + "event/view-match-event/", 23),
    EVENT_DELETE(C_BASE_URL + "event/destroy/", 23),
    EVENT_PLAYER_LIST(C_BASE_URL + "event/player-list", 23),
    SESSION_LIST(C_BASE_URL + "manager-whiteboard-uphq-list", 23),
    MATCH_DAY_PLANNER(C_BASE_URL + "matchday-planner", 23),
    CALENDER_MATCH_CREATE(C_BASE_URL + "calender/match-store", 23),
    CALENDER_MATCH_UPDATE(C_BASE_URL + "match-event/update/", 23),
    CALENDER_TRAINIG_CRAETE(C_BASE_URL + "event/store", 23),
    CALENDER_TRAINING_UPDATE(C_BASE_URL + "training-event/update/", 23),
    PLAYER_LOGIN(C_BASE_URL + "player-login", 23),
    HIGHLIGHTS(C_BASE_URL + "highlight", 23),
    HIGHLIGHTS_DELETE(C_BASE_URL + "highlight/delete", 23),
    CHAT(C_BASE_URL + "chat", 23),
    SEND_MEDIA(C_BASE_URL + "highlights/send-media", 23),
    UPDATE_MEDIA(C_BASE_URL + "highlights/update-media", 23),
    SEND_MEDIA_LINK(C_BASE_URL + "highlights/send-media-image", 23),
    UPDATE_MEDIA_LINK(C_BASE_URL + "highlights/update-media-image", 23),
    SEND_CHAT_MSG(C_BASE_URL + "chat/send-message", 23),
    SEND_CHAT_IMAGE(C_BASE_URL + "chat/send-image", 23),
    SEND_CHAT_VIDEO(C_BASE_URL + "chat/send-video", 23),
    SEND_CHAT_MEDIA_LINK(C_BASE_URL + "chat/send-message", 23),
    CHAT_NAME_UPDATE(C_BASE_URL + "chat/update-chat-name", 23),
    LOGOUT(C_BASE_URL + "logout", 23),
    ;

    private final String stringValue;
    private final int intValue;

    Api(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
