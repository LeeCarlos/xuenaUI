import { Button } from 'antd'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useStore } from '../../store'

export default function Header() {
  const { user, logout } = useStore()

  return (
    <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '0 24px', height: '64px', borderBottom: '1px solid #f0f0f0', backgroundColor: '#fff' }}>
      <h1 style={{ margin: 0, fontSize: '18px', fontWeight: 600 }}>供应商绩效分析系统</h1>
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        <span style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <UserOutlined />
          {user?.realName || user?.username}
        </span>
        <Button icon={<LogoutOutlined />} onClick={logout} danger>
          退出登录
        </Button>
      </div>
    </header>
  )
}