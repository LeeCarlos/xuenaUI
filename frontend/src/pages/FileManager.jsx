import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm, Upload } from 'antd'
import { UploadOutlined, DownloadOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons'
import fileService from '../services/file'
import { formatDate } from '../utils/format'

export default function FileManager() {
  const [data, setData] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [form] = Form.useForm()
  const uploadRef = useRef(null)

  const fetchData = async () => {
    try {
      const formValues = form.getFieldsValue()
      const params = {
        fileType: formValues.fileType || '',
        fileName: formValues.fileName || ''
      }
      const res = await fileService.list(params)
      setData(res.data || [])
    } catch {
      message.error('获取文件列表失败')
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const handleUpload = async (file) => {
    try {
      const formValues = form.getFieldsValue()
      await fileService.upload(file, formValues.fileType || 'NORMAL', formValues.description)
      message.success('上传成功')
      fetchData()
    } catch {
      message.error('上传失败')
    }
    return false
  }

  const handleDownloadByStoreKey = async (record) => {
    try {
      await fileService.downloadByStoreKey(record.storeKey)
    } catch {
      message.error('下载失败')
    }
  }

  const handleDownloadByFileName = async (record) => {
    try {
      await fileService.downloadByFileName(record.fileName)
    } catch {
      message.error('下载失败')
    }
  }

  const handleDelete = async (id) => {
    try {
      await fileService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
      message.error('删除失败')
    }
  }

  const formatFileSize = (size) => {
    if (!size) return '-'
    if (size < 1024) return size + ' B'
    if (size < 1024 * 1024) return (size / 1024).toFixed(2) + ' KB'
    return (size / (1024 * 1024)).toFixed(2) + ' MB'
  }



  const columns = [
    { title: '文件名', dataIndex: 'fileName', key: 'fileName' },
    { title: '文件类型', dataIndex: 'fileType', key: 'fileType', render: (text) => (text === 'TEMPLATE' ? '模板文件' : '普通文件') },
    { title: '大小', dataIndex: 'fileSize', key: 'fileSize', render: formatFileSize },
    { title: '描述', dataIndex: 'description', key: 'description', render: (text) => text || '-' },
    { title: '存储键', dataIndex: 'storeKey', key: 'storeKey', ellipsis: true },
    { title: '创建时间', dataIndex: 'createDate', key: 'createDate', render: formatDate },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button size="small" icon={<DownloadOutlined />} onClick={() => handleDownloadByFileName(record)}>按文件名下载</Button>
          <Button size="small" onClick={() => handleDownloadByStoreKey(record)}>按storeKey下载</Button>
          <Popconfirm title="确定删除该文件？" onConfirm={() => handleDelete(record.id)}>
            <Button size="small" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>文件管理</h2>
      </div>

      <Form form={form} layout="inline" style={{ marginBottom: '16px' }}>
        <Form.Item name="fileName" label="文件名">
          <Input placeholder="请输入文件名" />
        </Form.Item>
        <Form.Item name="fileType" label="文件类型">
          <Select placeholder="请选择类型" allowClear>
            <Select.Option value="NORMAL">普通文件</Select.Option>
            <Select.Option value="TEMPLATE">模板文件</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item name="description" label="描述">
          <Input placeholder="文件描述" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '16px' }}>
        <Upload.Dragger
          accept=".xlsx,.xls,.doc,.docx,.pdf,.zip,.rar,.txt"
          beforeUpload={handleUpload}
          showUploadList={false}
          style={{ display: 'inline-block', marginBottom: 0 }}
        >
          <Button icon={<UploadOutlined />}>上传文件</Button>
        </Upload.Dragger>
      </div>

      <Table columns={columns} dataSource={data} rowKey="id" />
    </div>
  )
}