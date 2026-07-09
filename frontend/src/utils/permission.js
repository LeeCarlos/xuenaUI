export function hasPermission(permissionCode) {
  const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
  return permissions.includes(permissionCode)
}

export function hasRole(roleCode) {
  const roles = JSON.parse(localStorage.getItem('roles') || '[]')
  return roles.includes(roleCode)
}

export function isSuperAdmin() {
  return hasRole('SUPER_ADMIN')
}