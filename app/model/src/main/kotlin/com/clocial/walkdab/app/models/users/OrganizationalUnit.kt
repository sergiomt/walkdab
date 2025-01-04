package com.clocial.walkdab.app.models.users

interface OrganizationalUnit : com.clocial.walkdab.app.models.snippets.Auditable,
    com.clocial.walkdab.app.models.snippets.Deletable, com.clocial.walkdab.app.models.snippets.GloballyUnique {
    var name: String?
}