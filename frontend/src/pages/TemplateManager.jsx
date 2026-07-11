import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, Space, message, Popconfirm, Upload } from 'antd'
import { UploadOutlined, DownloadOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons'
import fileService from '../services/file'
import { formatDate } from '../utils/format'

export default function TemplateManager() {
  const [data, setData] = useState([])
  const [form] = Form.useForm()

  const fetchData = async () => {
    try {
      const formValues = form.getFieldsValue()
      const params = {
        fileType: 'TEMPLATE',
        fileName: formValues.fileName || ''
      }
      const res = await fileService.list(params)
      setData(res.data || [])
    } catch {
      message.error('获取模板列表失败')
    }
  }

  useEffect(() => {
    fetchData()
  }, [])

  const handleUpload = async (file) => {
    try {
      const formValues = form.getFieldsValue()
      await fileService.upload(file, 'TEMPLATE', formValues.description)
      message.success('上传成功')
      fetchData()
    } catch {
      message.error('上传失败')
    }
    return false
  }

  const handleDownload = async (record) => {
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
    { title: '模板名称', dataIndex: 'fileName', key: 'fileName' },
    { title: '大小', dataIndex: 'fileSize', key: 'fileSize', render: formatFileSize },
    { title: '描述', dataIndex: 'description', key: 'description', render: (text) => text || '-' },
    { title: '存储键', dataIndex: 'storeKey', key: 'storeKey', ellipsis: true },
    { title: '创建时间', dataIndex: 'createDate', key: 'createDate', render: formatDate },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Space>
          <Button size="small" icon={<DownloadOutlined />} onClick={() => handleDownload(record)}>下载</Button>
          <Popconfirm title="确定删除该模板？" onConfirm={() => handleDelete(record.id)}>
            <Button size="small" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </Space>
      )
    }
  ]

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '16px' }}>
        <h2>模板管理</h2>
      </div>

      <Form form={form} layout="inline" style={{ marginBottom: '16px' }}>
        <Form.Item name="fileName" label="模板名称">
          <Input placeholder="请输入模板名称" />
        </Form.Item>
        <Form.Item name="description" label="描述">
          <Input placeholder="模板描述" />
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
          <Button icon={<UploadOutlined />}>上传模板</Button>
        </Upload.Dragger>
      </div>

      <Table columns={columns} dataSource={data} rowKey="id" />
    </div>
  )
}