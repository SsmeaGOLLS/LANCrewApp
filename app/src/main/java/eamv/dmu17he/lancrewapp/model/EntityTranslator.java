package eamv.dmu17he.lancrewapp.model;

import java.time.LocalDateTime;

/**
 * Created by alexj on 1/12/2018.
 */

class EntityTranslator {



    private static final EntityTranslator ourInstance = new EntityTranslator();

    static EntityTranslator getInstance() {
        return ourInstance;
    }

    private EntityTranslator() {
    }

    public void addWakeUp(String userName, LocalDateTime time, String comment) {

    }
}
