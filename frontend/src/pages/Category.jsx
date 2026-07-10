import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, Space, message, Popconfirm } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import categoryService from '../services/category'

export default function Category() {
  const [data, setData] = useState([])
  const [form] = Form.useForm()
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)

  const fetchData = async () => {
    try {
      const res = await categoryService.list()
      setData(res.data || [])
    } catch {
      message.error('获取品类列表失败')
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const handleAdd = () => {
    form.resetFields()
    setEditingId(null)
    setModalVisible(true)
  }

  const handleEdit = (record) => {
    form.setFieldsValue(record)
    setEditingId(record.id)
    setModalVisible(true)
  }

  const handleDelete = async (id) => {
    try {
      await categoryService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
      message.error('删除失败')
    }
  }

  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await categoryService.update(editingId, values)
        message.success('更新成功')
      } else {
        await categoryService.create(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const columns = [
    { title: '品类名称', dataIndex: 'name', key: 'name' },
    { title: '描述', dataIndex: 'description', key: 'description' },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button icon={<EditOutlined />} size="small" onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
            <Button icon={<DeleteOutlined />} size="small" danger>删除</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>品类管理</h2>
        <Button icon={<PlusOutlined />} onClick={handleAdd}>新增品类</Button>
      </div>

      <Table columns={columns} dataSource={data} rowKey="id" />

      <Modal
        title={editingId ? '编辑品类' : '新增品类'}
        visible={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <Form form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item name="name" label="品类名称" rules={[{ required: true, message: '请输入品类名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input.TextArea />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">确定</Button>
            <Button onClick={() => setModalVisible(false)} style={{ marginLeft: '8px' }}>取消</Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}