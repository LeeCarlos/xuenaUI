import { useState, useEffect } from 'react'
import { Card, Row, Col, Statistic, Table, Select, Button, message } from 'antd'
import { UserOutlined, BarChartOutlined, CheckCircleFilled, AlertOutlined } from '@ant-design/icons'
import summaryService from '../services/summary'

const { Option } = Select

export default function Summary() {
  const [data, setData] = useState(null)
  const [yearMonth, setYearMonth] = useState('')

  const fetchData = async () => {
    try {
      const params = {}
      if (yearMonth) params.yearMonth = yearMonth
      const res = await summaryService.getSummary(params)
      setData(res.data)
    } catch {
      message.error('获取汇总数据失败')
    }
  }

  useEffect(() => {
    fetchData()
  }, [yearMonth])

  const departmentColumns = [
    { title: '部门', dataIndex: 'department', key: 'department' },
    { title: '打分次数', dataIndex: 'scoreCount', key: 'scoreCount' },
    { title: '平均分', dataIndex: 'avgScore', key: 'avgScore', render: v => v?.toFixed(2) },
  ]

  const categoryColumns = [
    { title: '类别', dataIndex: 'category', key: 'category' },
    { title: '供应商数量', dataIndex: 'supplierCount', key: 'supplierCount' },
    { title: '平均分', dataIndex: 'avgScore', key: 'avgScore', render: v => v?.toFixed(2) },
    { title: 'A级数量', dataIndex: 'gradeACount', key: 'gradeACount' },
    { title: 'D级数量', dataIndex: 'gradeDCount', key: 'gradeDCount' },
  ]

  if (!data) {
    return <div style={{ textAlign: 'center', padding: '50px' }}>加载中...</div>
  }

  return (
    <div>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '24px' }}>
        <h2>数据汇总</h2>
        <Select
          placeholder="选择月份"
          value={yearMonth}
          onChange={setYearMonth}
          style={{ width: '200px' }}
          allowClear
        >
          <Option value="">全部月份</Option>
        </Select>
      </div>

      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card>
            <Statistic title="供应商总数" value={data.totalSupplierCount} prefix={<UserOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="启用供应商" value={data.activeSupplierCount} prefix={<CheckCircleFilled />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="考核记录" value={data.assessmentCount} prefix={<BarChartOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="完成考核" value={data.completedAssessmentCount} prefix={<AlertOutlined />} />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col span={6}>
          <Card>
            <Statistic title="考核平均分" value={data.avgScore?.toFixed(2) || 0} precision={2} />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: '24px' }}>
        <Col span={12}>
          <Card title="部门汇总">
            <Table
              columns={departmentColumns}
              dataSource={data.departmentSummaries}
              rowKey="department"
              bordered
              pagination={false}
            />
          </Card>
        </Col>

        <Col span={12}>
          <Card title="类别汇总">
            <Table
              columns={categoryColumns}
              dataSource={data.categorySummaries}
              rowKey="category"
              bordered
              pagination={false}
            />
          </Card>
        </Col>
      </Row>
    </div>
  )
}
