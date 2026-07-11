import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm, Checkbox, Pagination, Upload } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, StopOutlined, CheckCircleFilled, UploadOutlined, DownloadOutlined } from '@ant-design/icons'
import poolService from '../services/pool'
import dictService from '../services/dict'
import { formatDate } from '../utils/format'

export default function SupplierPool() {
  const [data, setData] = useState([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [categories, setCategories] = useState([])
  const [statusOptions, setStatusOptions] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const [selectedRowKeys, setSelectedRowKeys] = useState([])
  const [detailVisible, setDetailVisible] = useState(false)
  const [detailData, setDetailData] = useState(null)
  const formRef = useRef(null)

  const fetchData = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        name: formValues.name || '',
        category: formValues.category || '',
        isDisabled: formValues.isDisabled
      }
      const res = await poolService.list({ ...filters, pageNum, pageSize })
      setData(res.data?.list || [])
      setTotal(res.data?.total || 0)
    } catch {
      message.error('获取供应商列表失败')
    }
  }

  const fetchDicts = async () => {
    try {
      const categoryRes = await dictService.list('category')
      setCategories(categoryRes.data || [])
      const statusRes = await dictService.list('is_disabled')
      setStatusOptions(statusRes.data || [])
    } catch {
      message.error('获取字典数据失败')
    }
  }

  useEffect(() => {
    fetchData()
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
      await poolService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
      message.error('删除失败')
    }
  }

  const handleEnable = async (id) => {
    try {
      await poolService.enable(id)
      message.success('已启用')
      fetchData()
    } catch {
      message.error('操作失败')
    }
  }

  const handleDisable = async (id) => {
    try {
      await poolService.disable(id)
      message.success('已禁用')
      fetchData()
    } catch {
      message.error('操作失败')
    }
  }

  const handleBatchEnable = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要启用的供应商')
      return
    }
    try {
      for (const id of selectedRowKeys) {
        await poolService.enable(id)
      }
      message.success(`已批量启用 ${selectedRowKeys.length} 个供应商`)
      setSelectedRowKeys([])
      fetchData()
    } catch {
      message.error('批量启用失败')
    }
  }

  const handleBatchDisable = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要禁用的供应商')
      return
    }
    try {
      for (const id of selectedRowKeys) {
        await poolService.disable(id)
      }
      message.success(`已批量禁用 ${selectedRowKeys.length} 个供应商`)
      setSelectedRowKeys([])
      fetchData()
    } catch {
      message.error('批量禁用失败')
    }
  }

  const handleBatchDelete = async () => {
    if (selectedRowKeys.length === 0) {
      message.warning('请选择要删除的供应商')
      return
    }
    try {
      await poolService.batchDelete(selectedRowKeys)
      message.success(`已批量删除 ${selectedRowKeys.length} 个供应商`)
      setSelectedRowKeys([])
      fetchData()
    } catch {
      message.error('批量删除失败')
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

  const handleImport = async (file) => {
    try {
      const formData = new FormData()
      formData.append('file', file)
      await poolService.import(formData)
      message.success('导入成功')
      fetchData()
    } catch {
      message.error('导入失败')
    }
    return false
  }

  const handleExport = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        name: formValues.name || '',
        category: formValues.category || '',
        isDisabled: formValues.isDisabled
      }
      await poolService.export(filters)
    } catch {
      message.error('导出失败')
    }
  }

  const handleExportTemplate = async () => {
    try {
      await poolService.exportTemplate()
    } catch {
      message.error('导出模板失败')
    }
  }

  const handleShowDetail = async (record) => {
    try {
      const res = await poolService.get(record.id)
      setDetailData(res.data)
      setDetailVisible(true)
    } catch {
      message.error('获取详情失败')
    }
  }

  const columns = [
    {
      title: (
        <Checkbox
          checked={selectedRowKeys.length === data.length && data.length > 0}
          indeterminate={selectedRowKeys.length > 0 && selectedRowKeys.length < data.length}
          onChange={(e) => {
            setSelectedRowKeys(e.target.checked ? data.map((item) => item.id) : [])
          }}
        />
      ),
      dataIndex: 'id',
      key: 'id',
      render: (id) => (
        <Checkbox
          checked={selectedRowKeys.includes(id)}
          onChange={(e) => {
            if (e.target.checked) {
              setSelectedRowKeys([...selectedRowKeys, id])
            } else {
              setSelectedRowKeys(selectedRowKeys.filter((key) => key !== id))
            }
          }}
        />
      ),
    },
    { 
      title: '供应商名称', 
      dataIndex: 'name', 
      key: 'name',
      render: (text, record) => (
        <a onClick={() => handleShowDetail(record)} style={{ color: '#1890ff', cursor: 'pointer' }}>
          {text}
        </a>
      )
    },
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
        </Space>
      ),
    },
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>供应商池</h2>
      </div>
      
      <Form ref={formRef} layout="inline" style={{ marginBottom: '16px' }}>
        <Form.Item name="name" label="供应商名称：">
          <Input placeholder="请输入供应商名称" />
        </Form.Item>
        <Form.Item name="category" label="品类：">
          <Select placeholder="请选择品类" allowClear style={{ width: 200 }}>
            {categories.map((c) => (
              <Select.Option key={c.key} value={c.key}>{c.value}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item name="isDisabled" label="状态：">
          <Select placeholder="请选择状态" allowClear>
            {statusOptions.map((s) => (
              <Select.Option key={s.key} value={parseInt(s.key)}>{s.value}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <div style={{ display: 'flex', justifyContent: 'flex-end', alignItems: 'center', marginBottom: '16px' }}>
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExport}>导出</Button>
          <Button onClick={handleExportTemplate}>导出模板</Button>
          <Upload.Dragger
            accept=".xlsx,.xls"
            beforeUpload={handleImport}
            showUploadList={false}
            style={{ display: 'inline-block', marginBottom: 0 }}
          >
            <Button icon={<UploadOutlined />}>导入</Button>
          </Upload.Dragger>
          <Button icon={<CheckCircleFilled />} onClick={handleBatchEnable} disabled={selectedRowKeys.length === 0}>批量启用</Button>
          <Button icon={<StopOutlined />} onClick={handleBatchDisable} disabled={selectedRowKeys.length === 0}>批量禁用</Button>
          <Button icon={<DeleteOutlined />} onClick={handleBatchDelete} disabled={selectedRowKeys.length === 0} danger>批量删除</Button>
          <Button icon={<PlusOutlined />} onClick={handleAdd}>新增</Button>
        </Space>
      </div>

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
        title={editingId ? '编辑供应商' : '新增供应商'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <SupplierForm categories={categories} editingId={editingId} data={data} onSubmit={handleSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>

      <Modal
        title="供应商详情"
        open={detailVisible}
        onCancel={() => setDetailVisible(false)}
        footer={null}
      >
        {detailData && (
          <div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>供应商名称：</label>
              <span>{detailData.name}</span>
            </div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>品类：</label>
              <span>{detailData.category}</span>
            </div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>状态：</label>
              <span>{detailData.isDisabled === 0 ? '启用' : '禁用'}</span>
            </div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>创建人：</label>
              <span>{detailData.createName || '-'}</span>
            </div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>创建时间：</label>
              <span>{formatDate(detailData.createDate)}</span>
            </div>
            <div style={{ marginBottom: '16px' }}>
              <label style={{ fontWeight: 'bold', display: 'inline-block', width: '100px' }}>更新时间：</label>
              <span>{formatDate(detailData.updateDate)}</span>
            </div>
          </div>
        )}
      </Modal>
    </div>
  )
}

function SupplierForm({ categories, editingId, data, onSubmit, onCancel }) {
  const [form] = Form.useForm()
  
  useEffect(() => {
    if (editingId) {
      const record = data.find((item) => item.id == editingId)
      if (record) {
        form.setFieldsValue(record)
      }
    } else {
      form.resetFields()
    }
  }, [editingId, data, form])

  return (
    <Form form={form} onFinish={onSubmit} layout="vertical">
      <Form.Item name="name" label="供应商名称" rules={[{ required: true, message: '请输入供应商名称' }]}>
        <Input />
      </Form.Item>
      <Form.Item name="category" label="品类" rules={[{ required: true, message: '请选择品类' }]}>
        <Select>
          {categories.map((c) => (
            <Select.Option key={c.key} value={c.key}>{c.value}</Select.Option>
          ))}
        </Select>
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit">确定</Button>
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>取消</Button>
      </Form.Item>
    </Form>
  )
}