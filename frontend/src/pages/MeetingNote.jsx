import { useState, useEffect, useRef } from 'react'
import { Table, Button, Modal, Form, Input, Select, Space, message, Popconfirm, Pagination } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined, DownloadOutlined } from '@ant-design/icons'
import meetingNoteService from '../services/meetingNote'
import poolService from '../services/pool'

export default function MeetingNote() {
  const [data, setData] = useState([])
  const [total, setTotal] = useState(0)
  const [pageNum, setPageNum] = useState(1)
  const [pageSize, setPageSize] = useState(10)
  const [suppliers, setSuppliers] = useState([])
  const [modalVisible, setModalVisible] = useState(false)
  const [editingId, setEditingId] = useState(null)
  const formRef = useRef(null)

  const fetchData = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        supplierName: formValues.supplierName || ''
      }
      const res = await meetingNoteService.list({ ...filters, pageNum, pageSize })
      setData(res.data?.list || [])
      setTotal(res.data?.total || 0)
    } catch {
      message.error('获取会议纪要失败')
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

  useEffect(() => {
    fetchData()
    fetchSuppliers()
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
      await meetingNoteService.delete(id)
      message.success('删除成功')
      fetchData()
    } catch {
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

  const handleExport = async () => {
    try {
      const formValues = formRef.current?.getFieldsValue() || {}
      const filters = {
        supplierName: formValues.supplierName || ''
      }
      await meetingNoteService.export(filters)
    } catch {
      message.error('导出失败')
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
        <Space>
          <Button icon={<DownloadOutlined />} onClick={handleExport}>导出Excel</Button>
          <Button icon={<PlusOutlined />} onClick={handleAdd}>新增纪要</Button>
        </Space>
      </div>
      
      <Form ref={formRef} layout="inline">
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
        title={editingId ? '编辑会议纪要' : '新增会议纪要'}
        open={modalVisible}
        onCancel={() => setModalVisible(false)}
        footer={null}
      >
        <MeetingNoteForm suppliers={suppliers} editingId={editingId} data={data} onSubmit={handleSubmit} onCancel={() => setModalVisible(false)} />
      </Modal>
    </div>
  )
}

function MeetingNoteForm({ suppliers, editingId, data, onSubmit, onCancel }) {
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
        <Button onClick={onCancel} style={{ marginLeft: '8px' }}>取消</Button>
      </Form.Item>
    </Form>
  )
}
