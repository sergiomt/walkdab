package com.clocial.walkdab.app.models.users

import com.clocial.walkdab.app.models.snippets.*

interface UniqueUser : Auditable, Deletable, GloballyUnique, Identifiable, Named