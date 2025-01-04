package com.clocial.walkdab.app.pojos

import com.clocial.walkdab.app.models.users.SemanticNameSpace

open class SemanticNameSpacePojo : AbstractAuditableGuidPojo(), SemanticNameSpace {
    private var namespaceName: String = ""

    override fun equals(semanticNameSpace: Any?): Boolean {
        return semanticNameSpace is SemanticNameSpace && namespaceName != null && namespaceName == semanticNameSpace.getName()
    }

    override fun getId(): String {
        return getKey()
    }

    fun setId(id: String) {
        setKey(id)
    }

    override fun getName(): String {
        return namespaceName
    }

    override fun setName(name: String) {
        namespaceName = name
    }
}