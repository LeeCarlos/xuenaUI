import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, message, Popconfirm } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined } from '@ant-design/icons'
import departmentScoreService from '../services/departmentScore'
import poolService from '../services/pool'

export default function DepartmentScore() {
  const [data, setData] = useState([])
  const [suppliers, setSuppliers] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [filters, setFilters] = useState({ yearMonth: '', supplierName: '', department: '' })

  const fetchData = async () => {
    try {
      const res = await departmentScoreService.list(filters)
      setData(res.data || [])
    } catch {
      message.error('获取部门打分记录失败')
    }
  }

  const fetchSuppliers = async () => {
    try {
      const res = await poolService.list()
      setSuppliers(res.data || [])
    } catch {
      message.error('获取供应商列表失败')
    }
  }

  useEffect(() => {
    fetchData()
    fetchSuppliers()
  }, [filters])

  const handleAdd = () => {
    setEditingId(null)
    setModalVisible(true)
  }

  const handleEdit = (record) => {
    setEditingId(record.id)
    setModalVisible(true)
  }

  const handleDelete = async (id) => {
    try {
      await departmentScoreService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
      message.error('删除失败')
    }
  }

  const handleSubmit = async (id) => {
    try {
      await departmentScoreService.submit(id)
      message.success('已提交')
      fetchData()
    } catch {
      message.error('提交失败')
    }
  }

  const handleComplete = async (id) => {
    try {
      await departmentScoreService.complete(id)
      message.success('已完成')
      fetchData()
    } catch {
      message.error('操作失败')
    }
  }

  const handleFormSubmit = async (values) => {
    try {
      if (editingId) {
        await departmentScoreService.update(editingId, values)
        message.success('更新成功')
      } else {
        await departmentScoreService.create(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const columns = [
    { title: '年月', dataIndex: 'yearMonth', key: 'yearMonth' },
    { title: '供应商名称', dataIndex: 'supplierName', key: 'supplierName' },
    { title: '部门', dataIndex: 'department', key: 'department' },
    { title: '维度组', dataIndex: 'dimensionGroup', key: 'dimensionGroup' },
    { title: '维度得分', dataIndex: 'dimensionScore', key: 'dimensionScore' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (text) => {
        const statusMap = { PENDING: '未开始', IN_PROGRESS: '进行中', COMPLETED: '已完成' }
        return statusMap[text] || text
      },
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          {record.status !== 'COMPLETED' && (
            <Button icon={<EditOutlined />} size="small" onClick={() => handleEdit(record)}>编辑</Button>
          )}
          {record.status !== 'COMPLETED' && (
            <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
              <Button icon={<DeleteOutlined />} size="small" danger>删除</Button>
            </Popconfirm>
          )}
          {record.status === 'PENDING' && (
            <Button icon={<CheckOutlined />} size="small" onClick={() => handleSubmit(record.id)}>提交</Button>
          )}
          {record.status !== 'COMPLETED' && (
            <Button size="small" onClick={() => handleComplete(record.id)}>完成</Button>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>部门打分明细</h2>
        <Button icon={<PlusOutlined />} onClick={handleAdd}>新增打分</Button>
      </div>
      
      <Form layout="inline" onValuesChange={(changedValues, allValues) => setFilters(allValues)} initialValues={filters}>
        <Form.Item name="yearMonth">
          <Input placeholder="年月 (如2024-01)" />
        </Form.Item>
        <Form.Item name="supplierName">
          <Select placeholder="供应商" allowClear>
            {suppliers.map((s) => (
              <Select.Option key={s.id} value={s.name}>{s.name}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="department">
          <Select placeholder="部门" allowClear>
            <Select.Option value="质量">质量部</Select.Option>
            <Select.Option value="计划">计划部</Select.Option>
            <Select.Option value="包开">包开部</Select.Option>
            <Select.Option value="采购">采购部</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <Table columns={columns} dataSource={data} rowKey="id" />

      <Modal
        title={editingId ? '编辑打分' : '新增打分'}
        visible={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <DepartmentScoreForm suppliers={suppliers} editingId={editingId} data={data} onSubmit={handleFormSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>
    </div>
  )
}

function DepartmentScoreForm({ suppliers, editingId, data, onSubmit, onCancel }) {
  const [form] = Form.useForm()
  
  useEffect(() => {
    if (editingId) {
      const record = data.find((item) => item.id === editingId)
      if (record) {
        form.setFieldsValue(record)
      }
    } else {
      form.resetFields()
    }
  }, [editingId, data, form])

  return (
    <Form form={form} onFinish={onSubmit} layout="vertical">
      <Form.Item name="yearMonth" label="年月" rules={[{ required: true, message: '请输入年月' }]}>
        <Input placeholder="如2024-01" />
      </Form.Item>
      <Form.Item name="supplierName" label="供应商名称" rules={[{ required: true, message: '请选择供应商' }]}>
        <Select>
          {suppliers.map((s) => (
            <Select.Option key={s.id} value={s.name}>{s.name}</Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item name="department" label="部门" rules={[{ required: true, message: '请选择部门' }]}>
        <Select>
          <Select.Option value="质量">质量部</Select.Option>
          <Select.Option value="计划">计划部</Select.Option>
          <Select.Option value="包开">包开部</Select.Option>
          <Select.Option value="采购">采购部</Select.Option>
        </Select>
      </Form.Item>
      <Form.Item name="dimensionGroup" label="维度组">
        <Input />
      </Form.Item>
      <Form.Item name="dimensionScore" label="维度得分">
        <InputNumber min={0} style={{ width: '100%' }} />
      </Form.Item>
      <Form.Item name="subScores" label="子项得分(JSON)">
        <Input.TextArea />
      </Form.Item>
      <Form.Item name="exceptionReason" label="异常原因">
        <Input.TextArea />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">确定</Button>
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>取消</Button>
      </Form.Item>
    </Form>
  )
}
