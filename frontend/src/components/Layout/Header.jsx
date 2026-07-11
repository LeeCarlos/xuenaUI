import { Button, Dropdown, Avatar, Descriptions } from 'antd'
import { UserOutlined, LogoutOutlined } from '@ant-design/icons'
import { useStore } from '../../store'

export default function Header() {
  const { user, logout } = useStore()

  const menuItems = [
    {
      key: 'profile',
      label: (
        <div style={{ padding: '16px', width: '280px' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '12px', marginBottom: '16px' }}>
            <Avatar size={56} icon={<UserOutlined />} style={{ backgroundColor: '#1890ff' }} />
            <div>
              <div style={{ fontWeight: 600, fontSize: '16px' }}>{user?.realName || user?.username}</div>
              <div style={{ color: '#8c8c8c', fontSize: '12px' }}>{user?.username}</div>
            </div>
          </div>
          <Descriptions column={1} size="small" bordered>
            <Descriptions.Item label="部门">{user?.department || '-'}</Descriptions.Item>
            <Descriptions.Item label="角色">{user?.roles?.join(', ') || '-'}</Descriptions.Item>
          </Descriptions>
        </div>
      ),
    },
    { type: 'divider' },
    {
      key: 'logout',
      label: (
        <Button type="text" icon={<LogoutOutlined />} onClick={logout} danger block>
          退出登录
        </Button>
      ),
    },
  ]

  return (
    <header style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '0 24px', height: '64px', borderBottom: '1px solid #f0f0f0', backgroundColor: '#fff' }}>
      <h1 style={{ margin: 0, fontSize: '18px', fontWeight: 600 }}>供应商绩效分析系统</h1>
      <Dropdown menu={{ items: menuItems }} placement="bottomRight">
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px', cursor: 'pointer', padding: '4px 8px', borderRadius: '4px', hover: { backgroundColor: '#f5f5f5' } }}>
          <Avatar size={36} icon={<UserOutlined />} style={{ backgroundColor: '#1890ff' }} />
          <span>{user?.realName || user?.username}</span>
        </div>
      </Dropdown>
    </header>
  )
}