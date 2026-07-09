import { useEffect, useState } from 'react'
import { Card, Row, Col, Statistic } from 'antd'
import { UserOutlined, TrendingUpOutlined, FileTextOutlined, AlertOutlined } from '@ant-design/icons'
import poolService from '../services/pool'
import assessmentService from '../services/assessment'

export default function Dashboard() {
  const [stats, setStats] = useState({
    supplierCount: 0,
    activeSupplierCount: 0,
    assessmentCount: 0,
    pendingAssessmentCount: 0,
  })

  useEffect(() => {
    fetchStats()
  }, [])

  const fetchStats = async () => {
    try {
      const [supplierRes, assessmentRes] = await Promise.all([
        poolService.list(),
        assessmentService.list(),
      ])

      const suppliers = supplierRes.data || []
      const assessments = assessmentRes.data || []

      setStats({
        supplierCount: suppliers.length,
        activeSupplierCount: suppliers.filter((s) => s.isDisabled === 0).length,
        assessmentCount: assessments.length,
        pendingAssessmentCount: assessments.filter((a) => a.status === 'DRAFT' || a.status === 'SUBMITTED').length,
      })
    } catch (error) {
      console.error('Failed to fetch stats:', error)
    }
  }

  return (
    <div>
      <h2 style={{ marginBottom: '24px' }}>总览</h2>
      <Row gutter={[16, 16]}>
        <Col span={6}>
          <Card>
            <Statistic title="供应商总数" value={stats.supplierCount} prefix={<UserOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="启用供应商" value={stats.activeSupplierCount} prefix={<TrendingUpOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="考核记录" value={stats.assessmentCount} prefix={<FileTextOutlined />} />
          </Card>
        </Col>
        <Col span={6}>
          <Card>
            <Statistic title="待处理考核" value={stats.pendingAssessmentCount} prefix={<AlertOutlined />} />
          </Card>
        </Col>
      </Row>
    </div>
  )
}