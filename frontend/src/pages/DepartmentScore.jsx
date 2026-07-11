import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, message, Popconfirm, Pagination, DatePicker } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined } from '@ant-design/icons'
import dayjs from 'dayjs'
import zhCN from 'antd/locale/zh_CN'
import departmentScoreService from '../services/departmentScore'
import poolService from '../services/pool'
import dictService from '../services/dict'

export default function DepartmentScore() {
  const [data, setData] = useState([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [suppliers, setSuppliers] = useState([])
  const [departmentOptions, setDepartmentOptions] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const formRef = useRef(null)

  const fetchData = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        yearMonth: formValues.yearMonth ? formValues.yearMonth.format('YYYY-MM') : '',
        supplierName: formValues.supplierName || '',
        department: formValues.department || ''
      }
      const res = await departmentScoreService.list({ ...filters, pageNum, pageSize })
      setData(res.data?.list || [])
      setTotal(res.data?.total || 0)
    } catch {
      message.error('获取部门打分记录失败')
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
      const deptRes = await dictService.list('department')
      setDepartmentOptions(deptRes.data || [])
    } catch {
      message.error('获取字典数据失败')
    }
  }

  useEffect(() => {
    fetchData()
    fetchSuppliers()
    fetchDicts()
  }, [pageNum, pageSize])

  useEffect(() => {
    const lastMonth = dayjs().subtract(1, 'month')
    formRef.current?.setFieldsValue({ yearMonth: lastMonth })
  }, [])

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
      const submitValues = {
        ...values,
        yearMonth: values.yearMonth ? values.yearMonth.format('YYYY-MM') : ''
      }
      if (editingId) {
        await departmentScoreService.update(editingId, submitValues)
        message.success('更新成功')
      } else {
        await departmentScoreService.create(submitValues)
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
        yearMonth: formValues.yearMonth ? formValues.yearMonth.format('YYYY-MM') : '',
        supplierName: formValues.supplierName || '',
        department: formValues.department || ''
      }
      await departmentScoreService.export(filters)
    } catch {
      message.error('导出失败')
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
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExport}>导出Excel</Button>
          <Button icon={<PlusOutlined />} onClick={handleAdd}>新增打分</Button>
        </Space>
      </div>
      
      <Form ref={formRef} layout="inline">
        <Form.Item name="yearMonth">
          <DatePicker picker="month" format="YYYY-MM" placeholder="请选择年月" style={{ width: 180 }} locale={zhCN} />
        </Form.Item>
        <Form.Item name="supplierName">
          <Select placeholder="供应商" allowClear>
            {suppliers.map((s) => (
              <Select.Option key={s.id} value={s.name}>{s.name}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="department" label="部门">
          <Select placeholder="请选择部门" allowClear>
            {departmentOptions.map((d) => (
              <Select.Option key={d.key} value={d.key}>{d.value}</Select.Option>
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
        title={editingId ? '编辑打分' : '新增打分'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <DepartmentScoreForm suppliers={suppliers} departmentOptions={departmentOptions} editingId={editingId} data={data} onSubmit={handleFormSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>
    </div>
  )
}

function DepartmentScoreForm({ suppliers, departmentOptions, editingId, data, onSubmit, onCancel }) {
  const [form] = Form.useForm()
  
  useEffect(() => {
    if (editingId) {
      const record = data.find((item) => item.id === editingId)
      if (record) {
        const formattedRecord = {
          ...record,
          yearMonth: record.yearMonth ? dayjs(record.yearMonth, 'YYYY-MM') : null
        }
        form.setFieldsValue(formattedRecord)
      }
    } else {
      const lastMonth = dayjs().subtract(1, 'month')
      form.setFieldsValue({ yearMonth: lastMonth })
    }
  }, [editingId, data, form])

  return (
    <Form form={form} onFinish={onSubmit} layout="vertical">
      <Form.Item name="yearMonth" label="年月" rules={[{ required: true, message: '请选择年月' }]}>
        <DatePicker picker="month" format="YYYY-MM" style={{ width: '100%' }} locale={zhCN} />
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
          {departmentOptions.map((d) => (
            <Select.Option key={d.key} value={d.key}>{d.value}</Select.Option>
          ))}
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
