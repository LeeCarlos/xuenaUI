import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, Space, message, Popconfirm } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import meetingNoteService from '../services/meetingNote'
import poolService from '../services/pool'

export default function MeetingNote() {
  const [data, setData] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [form] = Form.useForm()
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [filters, setFilters] = useState({ supplierName: '' })

  useEffect(() => {
    fetchData()
    fetchSuppliers()
  }, [filters])

  const fetchData = async () => {
    try {
      const res = await meetingNoteService.list(filters)
      setData(res.data || [])
    } catch (error) {
      message.error('获取会议纪要失败')
    }
  }

  const fetchSuppliers = async () => {
    try {
      const res = await poolService.list()
      setSuppliers(res.data || [])
    } catch (error) {
      message.error('获取供应商列表失败')
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
      await meetingNoteService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch (error) {
      message.error('删除失败')
    }
  }

  const handleSubmit = async (values) => {
    try {
      if (editingId) {
        await meetingNoteService.update(editingId, values)
        message.success('更新成功')
      } else {
        await meetingNoteService.create(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const columns = [
    { title: '供应商名称', dataIndex: 'supplierName', key: 'supplierName' },
    { title: '月份范围-起始', dataIndex: 'monthFrom', key: 'monthFrom' },
    { title: '月份范围-结束', dataIndex: 'monthTo', key: 'monthTo' },
    { title: '纪要内容', dataIndex: 'note', key: 'note', ellipsis: true },
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
        <h2>会议纪要管理</h2>
        <Button icon={<PlusOutlined />} onClick={handleAdd}>新增纪要</Button>
      </div>
      
      <Form layout="inline" onValuesChange={(changedValues, allValues) => setFilters(allValues)} initialValues={filters}>
        <Form.Item name="supplierName">
          <Select placeholder="供应商" allowClear>
            {suppliers.map((s) => (
              <Select.Option key={s.id} value={s.name}>{s.name}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <Table columns={columns} dataSource={data} rowKey="id" />

      <Modal
        title={editingId ? '编辑会议纪要' : '新增会议纪要'}
        visible={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <Form form={form} onFinish={handleSubmit} layout="vertical">
          <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true, message: '请选择供应商' }]}>
            <Select>
              {suppliers.map((s) => (
                <Select.Option key={s.id} value={s.name}>{s.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="monthFrom" label="月份范围-起始" rules={[{ required: true, message: '请输入起始月份' }]}>
            <Input placeholder="如2024-01" />
          </Form.Item>
          <Form.Item name="monthTo" label="月份范围-结束" rules={[{ required: true, message: '请输入结束月份' }]}>
            <Input placeholder="如2024-03" />
          </Form.Item>
          <Form.Item name="note" label="纪要内容" rules={[{ required: true, message: '请输入纪要内容' }]}>
            <Input.TextArea rows={4} />
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