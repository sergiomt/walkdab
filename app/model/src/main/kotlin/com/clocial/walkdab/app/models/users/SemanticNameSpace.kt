package com.clocial.walkdab.app.models.users

import java.io.Serializable

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.Named

interface SemanticNameSpace : Auditable, GloballyUnique, Named, Serializable