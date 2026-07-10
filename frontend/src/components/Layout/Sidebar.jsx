import { useState } from 'react'
import { Menu } from 'antd'
import { useNavigate, useLocation } from 'react-router-dom'
import {
  DashboardOutlined,
  BarChartOutlined,
  ArrowUpOutlined,
  FileTextOutlined,
  DatabaseOutlined,
  EditOutlined,
  SettingOutlined,
  UserOutlined,
  FolderOutlined,
  KeyOutlined,
} from '@ant-design/icons'
import { useStore } from '../../store'

const menuIconMap = {
  DashboardOutlined: DashboardOutlined,
  BarChartOutlined: BarChartOutlined,
  ArrowUpOutlined: ArrowUpOutlined,
  FileTextOutlined: FileTextOutlined,
  DatabaseOutlined: DatabaseOutlined,
  EditOutlined: EditOutlined,
  SettingOutlined: SettingOutlined,
  UserOutlined: UserOutlined,
  FolderOutlined: FolderOutlined,
  KeyOutlined: KeyOutlined,
  Dashboard: DashboardOutlined,
  User: UserOutlined,
  BarChart: BarChartOutlined,
}

function renderMenuItems(menus, navigate) {
  return menus.map((menu) => {
    const Icon = menuIconMap[menu.icon] || SettingOutlined
    
    if (menu.children && menu.children.length > 0) {
      return {
        key: menu.id.toString(),
        label: menu.name,
        icon: <Icon />,
        children: renderMenuItems(menu.children, navigate),
      }
    }
    
    return {
      key: menu.id.toString(),
      label: menu.name,
      icon: <Icon />,
      onClick: () => navigate(menu.path),
    }
  })
}

export default function Sidebar() {
  const { menus } = useStore()
  const navigate = useNavigate()
  const location = useLocation()
  const [collapsed, setCollapsed] = useState(false)

  const defaultOpenKeys = menus.filter((m) => m.children && m.children.length > 0).map((m) => m.id.toString())
  const defaultSelectedKey = menus.find((m) => m.path === location.pathname)?.id.toString()

  return (
    <aside style={{ width: collapsed ? '64px' : '200px', backgroundColor: '#001529', minHeight: 'calc(100vh - 64px)', color: '#fff', transition: 'width 0.3s' }}>
      <div style={{ padding: '16px', fontSize: '14px', textAlign: 'center', borderBottom: '1px solid #1890ff' }}>
        {!collapsed && <span>绩效分析</span>}
      </div>
      <Menu
        mode="inline"
        theme="dark"
        defaultOpenKeys={defaultOpenKeys}
        defaultSelectedKeys={defaultSelectedKey ? [defaultSelectedKey] : []}
        style={{ borderRight: 0 }}
        items={renderMenuItems(menus, navigate)}
      />
      <button
        onClick={() => setCollapsed(!collapsed)}
        style={{ position: 'absolute', right: '-16px', top: '200px', width: '32px', height: '32px', borderRadius: '50%', backgroundColor: '#1890ff', border: 'none', cursor: 'pointer', color: '#fff', display: 'flex', alignItems: 'center', justifyContent: 'center' }}
      >
        {collapsed ? '→' : '←'}
      </button>
    </aside>
  )
}