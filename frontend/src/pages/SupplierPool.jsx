import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, BanOutlined, CheckCircleOutlined } from '@ant-design/icons'
import poolService from '../services/pool'
import categoryService from '../services/category'

export default function SupplierPool() {
  const [data, setData] = useState([])
  const [categories, setCategories] = useState([])
  const [form] = Form.useForm()
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [filters, setFilters] = useState({ name: '', category: '', isDisabled: undefined })

  useEffect(() => {
    fetchData()
    fetchCategories()
  }, [filters])

  const fetchData = async () => {
    try {
      const res = await poolService.list(filters)
      setData(res.data || [])
    } catch (error) {
      message.error('获取供应商列表失败')
    }
  }

  const fetchCategories = async () => {
    try {
      const res = await categoryService.list()
      setCategories(res.data || [])
    } catch (error) {
      message.error('获取品类列表失败')
    }
  }

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
      await poolService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const handleEnable = async (id) => {
    try {
      await poolService.enable(id)
      message.success('已启用')
      fetchData()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleDisable = async (id) => {
    try {
      await poolService.disable(id)
      message.success('已禁用')
      fetchData()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await poolService.update(editingId, values)
        message.success('更新成功')
      } else {
        await poolService.create(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const columns = [
    { title: '供应商名称', dataIndex: 'name', key: 'name' },
    { title: '品类', dataIndex: 'category', key: 'category' },
    {
      title: '状态',
      dataIndex: 'isDisabled',
      key: 'isDisabled',
      render: (text) => (text === 0 ? '启用' : '禁用'),
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button icon={<EditOutlined />} size="small" onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
            <Button icon={<DeleteOutlined />} size="small" danger>删除</Button>
          </Popconfirm>
          {record.isDisabled === 0 ? (
            <Button icon={<BanOutlined />} size="small" onClick={() => handleDisable(record.id)}>禁用</Button>
          ) : (
            <Button icon={<CheckCircleOutlined />} size="small" onClick={() => handleEnable(record.id)}>启用</Button>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>供应商池</h2>
        <Button icon={<PlusOutlined />} onClick={handleAdd}>新增供应商</Button>
      </div>
      
      <Form layout="inline" onValuesChange={(changedValues, allValues) => setFilters(allValues)} initialValues={filters}>
        <Form.Item name="name">
          <Input placeholder="供应商名称" />
        </Form.Item>
        <Form.Item name="category">
          <Select placeholder="选择品类" allowClear>
            {categories.map((c) => (
              <Select.Option key={c.id} value={c.name}>{c.name}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="isDisabled">
          <Select placeholder="状态" allowClear>
            <Select.Option value={0}>启用</Select.Option>
            <Select.Option value={1}>禁用</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <Table columns={columns} dataSource={data} rowKey="id" />

      <Modal
        title={editingId ? '编辑供应商' : '新增供应商'}
        visible={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <Form form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item name="name" label="供应商名称" rules={[{ required: true, message: '请输入供应商名称' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="category" label="品类" rules={[{ required: true, message: '请选择品类' }]}>
            <Select>
              {categories.map((c) => (
                <Select.Option key={c.id} value={c.name}>{c.name}</Select.Option>
              ))}
            </Select>
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