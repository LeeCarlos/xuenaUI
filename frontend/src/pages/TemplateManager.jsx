import { useState, useEffect } from 'react'
import { Table, Button, Modal, Form, Input, Space, message, Popconfirm, Upload, Select } from 'antd'
import { UploadOutlined, DownloadOutlined, DeleteOutlined, PlusOutlined } from '@ant-design/icons'
import fileService from '../services/file'
import { formatDate } from '../utils/format'

const BUSINESS_TYPES = [
  { value: 'SUPPLIER_POOL', label: '供应商池' },
  { value: 'DEPARTMENT_SCORE', label: '部门打分' },
  { value: 'ASSESSMENT', label: '考核管理' },
  { value: 'MEETING_NOTE', label: '会议纪要' },
]

export default function TemplateManager() {
  const [data, setData] = useState([])
  const [form] = Form.useForm()
  const [uploadModalVisible, setUploadModalVisible] = useState(false)
  const [uploadForm] = Form.useForm()

  const fetchData = async () => {
    try {
      const formValues = form.getFieldsValue()
      const params = {
        fileType: 'TEMPLATE',
        fileName: formValues.fileName || '',
        businessType: formValues.businessType || ''
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

  const handleUpload = async () => {
    try {
      const formValues = uploadForm.getFieldsValue()
      if (!formValues.file) {
        message.warning('请选择文件')
        return
      }
      if (!formValues.businessType) {
        message.warning('请选择业务类型')
        return
      }
      await fileService.upload(formValues.file.originFileObj, 'TEMPLATE', formValues.businessType, formValues.description)
      message.success('上传成功')
      setUploadModalVisible(false)
      uploadForm.resetFields()
      fetchData()
    } catch {
      message.error('上传失败')
    }
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
    { 
      title: '业务类型', 
      dataIndex: 'businessType', 
      key: 'businessType', 
      render: (text) => {
        const type = BUSINESS_TYPES.find(t => t.value === text)
        return type ? type.label : text || '-'
      }
    },
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
        <Form.Item name="businessType" label="业务类型">
          <Select placeholder="请选择业务类型" allowClear>
            {BUSINESS_TYPES.map((type) => (
              <Select.Option key={type.value} value={type.value}>{type.label}</Select.Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={fetchData}>搜索</Button>
        </Form.Item>
      </Form>

      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: '16px' }}>
        <Button icon={<UploadOutlined />} onClick={() => setUploadModalVisible(true)}>上传模板</Button>
      </div>

      <Table columns={columns} dataSource={data} rowKey="id" />

      <Modal
        title="上传模板"
        open={uploadModalVisible}
        onCancel={() => { setUploadModalVisible(false); uploadForm.resetFields(); }}
        footer={null}
      >
        <Form form={uploadForm} layout="vertical">
          <Form.Item name="businessType" label="业务类型" rules={[{ required: true, message: '请选择业务类型' }]}>
            <Select>
              {BUSINESS_TYPES.map((type) => (
                <Select.Option key={type.value} value={type.value}>{type.label}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="file" label="文件" rules={[{ required: true, message: '请选择文件' }]}>
            <Upload.Dragger
              accept=".xlsx,.xls"
              beforeUpload={() => false}
              onChange={(info) => {
                if (info.fileList.length > 0) {
                  uploadForm.setFieldsValue({ file: info.fileList[0] })
                }
              }}
              showUploadList={true}
            >
              <Button icon={<UploadOutlined />}>选择文件</Button>
            </Upload.Dragger>
          </Form.Item>
          <Form.Item name="description" label="描述">
            <Input placeholder="模板描述" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" onClick={handleUpload}>上传</Button>
            <Button onClick={() => { setUploadModalVisible(false); uploadForm.resetFields(); }} style={{ marginLeft: '8px' }}>取消</Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}