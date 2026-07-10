import { useContext } from 'react'
import { StoreContext } from '../store'

export function usePermission() {
  const { permissions, roles } = useContext(StoreContext)

  const hasPermission = (permissionCode) => permissions.includes(permissionCode)

  const hasRole = (roleCode) => roles.includes(roleCode)

  const isSuperAdmin = () => hasRole('SUPER_ADMIN')

  return { hasPermission, hasRole, isSuperAdmin }
}