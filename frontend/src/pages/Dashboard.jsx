import { useEffect, useState, useRef } from 'react'
import { Card, Row, Col, Statistic, Select, Checkbox } from 'antd'
import { UserOutlined, TrendingUpOutlined, FileTextOutlined, AlertOutlined } from '@ant-design/icons'
import * as echarts from 'echarts'
import poolService from '../services/pool'
import assessmentService from '../services/assessment'
import overviewService from '../services/overview'

const { Option } = Select
const { Group: CheckboxGroup } = Checkbox

export default function Dashboard() {
  const [stats, setStats] = useState({
    supplierCount: 0,
    activeSupplierCount: 0,
    assessmentCount: 0,
    pendingAssessmentCount: 0,
  })

  const [categories, setCategories] = useState([])
  const [selectedCategories, setSelectedCategories] = useState([])
  const [yearMonths, setYearMonths] = useState([])
  const [selectedYearMonths, setSelectedYearMonths] = useState([])
  const [aggregateType, setAggregateType] = useState('avg')
  const [trendData, setTrendData] = useState(null)
  const chartRef = useRef(null)
  const chartInstance = useRef(null)

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
    } catch {
      console.error('Failed to fetch stats')
    }
  }

  const fetchCategories = async () => {
    try {
      const res = await overviewService.getCategories()
      setCategories(res.data || [])
    } catch {
      console.error('Failed to fetch categories')
    }
  }

  const fetchYearMonths = async () => {
    try {
      const res = await overviewService.getYearMonths()
      setYearMonths(res.data || [])
    } catch {
      console.error('Failed to fetch year months')
    }
  }

  const fetchTrendData = async () => {
    try {
      const params = {
        aggregateType,
      }
      if (selectedCategories.length > 0) {
        params.categories = selectedCategories
      }
      if (selectedYearMonths.length > 0) {
        params.yearMonths = selectedYearMonths
      }
      const res = await overviewService.getTrend(params)
      setTrendData(res.data)
    } catch {
      console.error('Failed to fetch trend data')
    }
  }

  useEffect(() => {
    fetchStats()
    fetchCategories()
    fetchYearMonths()
  }, [])

  useEffect(() => {
    fetchTrendData()
  }, [selectedCategories, selectedYearMonths, aggregateType])

  useEffect(() => {
    if (!chartRef.current || !trendData) return

    if (!chartInstance.current) {
      chartInstance.current = echarts.init(chartRef.current)
    }

    const option = {
      tooltip: {
        trigger: 'axis',
        formatter: (params) => {
          let result = `<strong>${params[0].axisValue}</strong><br/>`
          params.forEach((item) => {
            result += `${item.marker} ${item.seriesName}: ${item.value}<br/>`
          })
          return result
        },
      },
      legend: {
        data: trendData.series.map((s) => s.name),
        bottom: 10,
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '15%',
        top: '10%',
        containLabel: true,
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: trendData.xAxisLabels,
      },
      yAxis: {
        type: 'value',
        name: aggregateType === 'avg' ? '平均分' : '总分',
      },
      series: trendData.series.map((s) => ({
        name: s.name,
        type: 'line',
        smooth: true,
        data: s.data,
        lineStyle: { width: 2 },
        itemStyle: {
          borderRadius: 4,
        },
      })),
    }

    chartInstance.current.setOption(option)

    const handleResize = () => {
      chartInstance.current?.resize()
    }
    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [trendData, aggregateType])

  const handleCategoryChange = (values) => {
    setSelectedCategories(values)
  }

  const handleYearMonthChange = (values) => {
    setSelectedYearMonths(values)
  }

  const handleAggregateTypeChange = (value) => {
    setAggregateType(value)
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

      <Card style={{ marginTop: '24px' }}>
        <div style={{ marginBottom: '16px' }}>
          <h3 style={{ marginBottom: '12px' }}>趋势数据分析</h3>
          <Row gutter={[16, 16]} align="middle">
            <Col span={8}>
              <div style={{ fontWeight: 500, marginBottom: '8px' }}>类别筛选</div>
              <CheckboxGroup
                options={categories.map((c) => ({ label: c, value: c }))}
                value={selectedCategories}
                onChange={handleCategoryChange}
              />
            </Col>
            <Col span={8}>
              <div style={{ fontWeight: 500, marginBottom: '8px' }}>月份筛选</div>
              <Select
                mode="multiple"
                placeholder="选择月份（默认全部）"
                value={selectedYearMonths}
                onChange={handleYearMonthChange}
                style={{ width: '100%' }}
                maxTagCount="responsive"
              >
                {yearMonths.map((month) => (
                  <Option key={month} value={month}>
                    {month}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={8}>
              <div style={{ fontWeight: 500, marginBottom: '8px' }}>聚合方式</div>
              <Select
                value={aggregateType}
                onChange={handleAggregateTypeChange}
                style={{ width: '100%' }}
              >
                <Option value="avg">平均分</Option>
                <Option value="sum">总分</Option>
              </Select>
            </Col>
          </Row>
        </div>
        <div ref={chartRef} style={{ height: '400px' }} />
      </Card>
    </div>
  )
}
