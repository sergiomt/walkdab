package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.util.str.Uid

abstract class AbstractAuditableGuidPojo : AbstractAuditablePojo, GloballyUnique {

    constructor() {
        setKey(Uid().createUniqueKey())
    }
}