import { createContext, useContext, useState } from 'react'

const StoreContext = createContext()

export function StoreProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('user')
    return saved ? JSON.parse(saved) : null
  })

  const [menus, setMenus] = useState(() => {
    const saved = localStorage.getItem('menus')
    return saved ? JSON.parse(saved) : []
  })

  const [permissions, setPermissions] = useState(() => {
    const saved = localStorage.getItem('permissions')
    return saved ? JSON.parse(saved) : []
  })

  const [roles, setRoles] = useState(() => {
    const saved = localStorage.getItem('roles')
    return saved ? JSON.parse(saved) : []
  })

  const login = (data) => {
    localStorage.setItem('token', data.token)
    localStorage.setItem('refreshToken', data.refreshToken)
    localStorage.setItem('user', JSON.stringify(data.user))
    localStorage.setItem('menus', JSON.stringify(data.menus))
    localStorage.setItem('permissions', JSON.stringify(data.user.permissions))
    localStorage.setItem('roles', JSON.stringify(data.user.roles))
    setUser(data.user)
    setMenus(data.menus)
    setPermissions(data.user.permissions)
    setRoles(data.user.roles)
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
    localStorage.removeItem('user')
    localStorage.removeItem('menus')
    localStorage.removeItem('permissions')
    localStorage.removeItem('roles')
    setUser(null)
    setMenus([])
    setPermissions([])
    setRoles([])
  }

  return (
    <StoreContext.Provider value={{ user, menus, permissions, roles, login, logout }}>
      {children}
    </StoreContext.Provider>
  )
}

export function useStore() {
  return useContext(StoreContext)
}