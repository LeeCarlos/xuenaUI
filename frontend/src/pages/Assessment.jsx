import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, message, Popconfirm, Pagination } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, LockOutlined, DownloadOutlined } from '@ant-design/icons'
import assessmentService from '../services/assessment'
import poolService from '../services/pool'
import dictService from '../services/dict'

export default function Assessment() {
  const [data, setData] = useState([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [suppliers, setSuppliers] = useState([])
  const [gradeOptions, setGradeOptions] = useState([])
  const [statusOptions, setStatusOptions] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const formRef = useRef(null)

  const fetchData = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        yearMonth: formValues.yearMonth || '',
        supplierName: formValues.supplierName || '',
        grade: formValues.grade || '',
        status: formValues.status || ''
      }
      const res = await assessmentService.list({ ...filters, pageNum, pageSize })
      setData(res.data?.list || [])
      setTotal(res.data?.total || 0)
    } catch {
      message.error('获取考核记录失败')
    }
  }

  const fetchSuppliers = async () => {
    try {
      const res = await poolService.list({ pageNum: 1, pageSize: 9999 })
      setSuppliers(res.data?.list || [])
    } catch {
      message.error('获取供应商列表失败')
    }
  }

  const fetchDicts = async () => {
    try {
      const gradeRes = await dictService.list('grade')
      setGradeOptions(gradeRes.data || [])
      const statusRes = await dictService.list('status')
      setStatusOptions(statusRes.data || [])
    } catch {
      message.error('获取字典数据失败')
    }
  }

  useEffect(() => {
    fetchData()
    fetchSuppliers()
    fetchDicts()
  }, [pageNum, pageSize])

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
      await assessmentService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
      message.error('删除失败')
    }
  }

  const handleSubmit = async (id) => {
    try {
      await assessmentService.submit(id)
      message.success('已提交')
      fetchData()
    } catch {
      message.error('提交失败')
    }
  }

  const handleLock = async (id) => {
    try {
      await assessmentService.lock(id)
      message.success('已锁定')
      fetchData()
    } catch {
      message.error('锁定失败')
    }
  }

  const handleFormSubmit = async (values) => {
    try {
      if (editingId) {
        await assessmentService.update(editingId, values)
        message.success('更新成功')
      } else {
        await assessmentService.create(values)
        message.success('创建成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const handleExport = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        yearMonth: formValues.yearMonth || '',
        supplierName: formValues.supplierName || '',
        grade: formValues.grade || '',
        status: formValues.status || ''
      }
      await assessmentService.export(filters)
    } catch {
      message.error('导出失败')
    }
  }

  const handleExportTemplate = async () => {
    try {
      await assessmentService.exportTemplate()
    } catch {
      message.error('导出模板失败')
    }
  }

  const columns = [
    { title: '年月', dataIndex: 'yearMonth', key: 'yearMonth' },
    { title: '供应商名称', dataIndex: 'supplierName', key: 'supplierName' },
    { title: '品类', dataIndex: 'category', key: 'category' },
    { title: '总分', dataIndex: 'total', key: 'total' },
    { title: '评级', dataIndex: 'grade', key: 'grade' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (text) => {
        const status = statusOptions.find(s => s.key === text)
        return status ? status.value : text
      },
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          {record.status !== 'LOCKED' && (
            <Button icon={<EditOutlined />} size="small" onClick={() => handleEdit(record)}>编辑</Button>
          )}
          {record.status !== 'LOCKED' && (
            <Popconfirm title="确定删除？" onConfirm={() => handleDelete(record.id)}>
              <Button icon={<DeleteOutlined />} size="small" danger>删除</Button>
            </Popconfirm>
          )}
          {record.status === 'DRAFT' && (
            <Button icon={<CheckOutlined />} size="small" onClick={() => handleSubmit(record.id)}>提交</Button>
          )}
          {record.status !== 'LOCKED' && (
            <Button icon={<LockOutlined />} size="small" onClick={() => handleLock(record.id)}>锁定</Button>
          )}
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>月度考核管理</h2>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExportTemplate}>导出模板</Button>
          <Button icon={<DownloadOutlined />} onClick={handleExport}>导出Excel</Button>
          <Button icon={<PlusOutlined />} onClick={handleAdd}>新增考核</Button>
        </Space>
      </div>
      
      <Form ref={formRef} layout="inline">
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
        <Form.Item name="grade" label="评级">
          <Select placeholder="请选择评级" allowClear>
            {gradeOptions.map((g) => (
              <Select.Option key={g.key} value={g.key}>{g.value}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="status" label="状态">
          <Select placeholder="请选择状态" allowClear>
            {statusOptions.map((s) => (
              <Select.Option key={s.key} value={s.key}>{s.value}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <Table columns={columns} dataSource={data} rowKey="id" pagination={false} />
      
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '16px' }}>
        <Pagination
          current={pageNum}
          pageSize={pageSize}
          total={total}
          showTotal={(total) => `共 ${total} 条记录`}
          onChange={(page, size) => {
            setPageNum(page)
            setPageSize(size)
          }}
          showSizeChanger
          pageSizeOptions={['10', '20', '30', '40', '50']}
          showSizeChangerTooltipRender={() => ''}
        />
      </div>

      <Modal
        title={editingId ? '编辑考核' : '新增考核'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
        width={800}
      >
        <AssessmentForm suppliers={suppliers} editingId={editingId} data={data} onSubmit={handleFormSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>
    </div>
  )
}

function AssessmentForm({ suppliers, editingId, data, onSubmit, onCancel }) {
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
      <Form.Item name="category" label="品类">
        <Input />
      </Form.Item>
      <Form.Item name="dimensionA" label="品质考核(A) 满分25">
        <InputNumber min={0} max={25} style={{ width: '100%' }} />
      </Form.Item>
      <Form.Item name="dimensionB" label="成本考核(B) 满分20">
        <InputNumber min={0} max={20} style={{ width: '100%' }} />
      </Form.Item>
      <Form.Item name="dimensionC" label="交货考核(C) 满分20">
        <InputNumber min={0} max={20} style={{ width: '100%' }} />
      </Form.Item>
      <Form.Item name="dimensionD" label="服务考核(D) 满分35">
        <InputNumber min={0} max={35} style={{ width: '100%' }} />
      </Form.Item>
      <Form.Item name="conclusion" label="结论">
        <Input.TextArea />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">确定</Button>
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>取消</Button>
      </Form.Item>
    </Form>
  )
}
