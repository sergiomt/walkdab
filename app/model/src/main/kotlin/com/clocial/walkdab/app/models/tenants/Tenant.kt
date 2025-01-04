package com.clocial.walkdab.app.models.tenants

import com.clocial.walkdab.app.models.snippets.Auditable
import com.clocial.walkdab.app.models.snippets.GloballyUnique
import com.clocial.walkdab.app.models.snippets.Named

interface Tenant : com.clocial.walkdab.app.models.snippets.Auditable, com.clocial.walkdab.app.models.snippets.Named,
    com.clocial.walkdab.app.models.snippets.GloballyUnique