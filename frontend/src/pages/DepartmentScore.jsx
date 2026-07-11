import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, InputNumber, Select, Space, message, Popconfirm, Pagination, DatePicker, Upload } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, DownloadOutlined, UploadOutlined } from '@ant-design/icons'
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
  const [importModalVisible, setImportModalVisible] = useState(false)
  const [selectedDeptForImport, setSelectedDeptForImport] = useState('')
  const formRef = useRef(null)
  const importFormRef = useRef(null)

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
      message.success('已提交，不可修改')
      fetchData()
    } catch {
      message.error('提交失败')
    }
  }

  const handleSave = async (values) => {
    try {
      const submitValues = {
        ...values,
        yearMonth: values.yearMonth ? values.yearMonth.format('YYYY-MM') : '',
        status: 'IN_PROGRESS'
      }
      if (editingId) {
        await departmentScoreService.update(editingId, submitValues)
        message.success('保存成功')
      } else {
        await departmentScoreService.create(submitValues)
        message.success('保存成功')
      }
      setModalVisible(false)
      fetchData()
    } catch (error) {
      message.error(error.message || '操作失败')
    }
  }

  const handleFormSubmit = async (values) => {
    try {
      const submitValues = {
        ...values,
        yearMonth: values.yearMonth ? values.yearMonth.format('YYYY-MM') : '',
        status: 'COMPLETED'
      }
      if (editingId) {
        await departmentScoreService.update(editingId, submitValues)
        message.success('提交成功，不可修改')
      } else {
        await departmentScoreService.create(submitValues)
        message.success('提交成功，不可修改')
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

  const handleExportTemplate = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const department = formValues.department
      if (!department) {
        message.warning('请先选择部门')
        return
      }
      await departmentScoreService.exportTemplate(department)
    } catch {
      message.error('导出模板失败')
    }
  }

  const handleImport = async (file) => {
    try {
      if (!selectedDeptForImport) {
        message.warning('请先选择部门')
        return false
      }
      const formData = new FormData()
      formData.append('file', file)
      formData.append('department', selectedDeptForImport)
      await departmentScoreService.batchImport(formData)
      message.success('导入成功')
      setImportModalVisible(false)
      setSelectedDeptForImport('')
      fetchData()
    } catch {
      message.error('导入失败')
    }
    return false
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
        const statusMap = { PENDING: '未提交', IN_PROGRESS: '已保存', COMPLETED: '已提交' }
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
          {record.status !== 'COMPLETED' && (
            <Button icon={<CheckOutlined />} size="small" onClick={() => handleSubmit(record.id)}>提交</Button>
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
          <Button icon={<DownloadOutlined />} onClick={handleExport}>导出</Button>
          <Button icon={<DownloadOutlined />} onClick={handleExportTemplate}>导出模板</Button>
          <Button icon={<UploadOutlined />} onClick={() => setImportModalVisible(true)}>导入打分</Button>
          <Button icon={<PlusOutlined />} onClick={handleAdd}>新增</Button>
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
        <DepartmentScoreForm suppliers={suppliers} departmentOptions={departmentOptions} editingId={editingId} data={data} onSave={handleSave} onSubmit={handleFormSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>

      <Modal
        title="批量导入打分"
        open={importModalVisible}
        onCancel={() => { setImportModalVisible(false); setSelectedDeptForImport(''); }}
        footer={null}
      >
        <Form ref={importFormRef} layout="vertical">
          <Form.Item label="部门" rules={[{ required: true, message: '请选择部门' }]}>
            <Select value={selectedDeptForImport} onChange={(value) => setSelectedDeptForImport(value)}>
              {departmentOptions.map((d) => (
                <Select.Option key={d.key} value={d.key}>{d.value}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item label="上传文件">
            <Upload.Dragger
              accept=".xlsx,.xls"
              beforeUpload={handleImport}
              showUploadList={false}
            >
              <Button icon={<UploadOutlined />}>选择文件</Button>
            </Upload.Dragger>
          </Form.Item>
          <Form.Item>
            <Button onClick={() => { setImportModalVisible(false); setSelectedDeptForImport(''); }}>取消</Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

function DepartmentScoreForm({ suppliers, departmentOptions, editingId, data, onSave, onSubmit, onCancel }) {
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

  const handleSave = async () => {
    try {
      const values = await form.validateFields()
      await onSave(values)
    } catch (error) {
      message.error('请填写必填项')
    }
  }

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields()
      await onSubmit(values)
    } catch (error) {
      message.error('请填写必填项')
    }
  }

  return (
    <Form form={form} layout="vertical">
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
        <Button type="primary" onClick={handleSave}>保存</Button>
        <Button onClick={handleSubmit} style={{ marginLeft: '8px' }}>提交</Button>
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>取消</Button>
      </Form.Item>
    </Form>
  )
}
