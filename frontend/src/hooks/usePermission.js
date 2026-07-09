import { useContext } from 'react'
import { StoreContext } from '../store'

export function usePermission() {
  const { permissions, roles } = useContext(StoreContext)

  const hasPermission = (permissionCode) => {
    return permissions.includes(permissionCode)
  }

  const hasRole = (roleCode) => {
    return roles.includes(roleCode)
  }

  const isSuperAdmin = () => {
    return hasRole('SUPER_ADMIN')
  }

  return { hasPermission, hasRole, isSuperAdmin }
}