import { useEffect, useState, useRef } from 'react'
import { Card, Row, Col, Statistic, Select, Checkbox, Table } from 'antd'
import { UserOutlined, ArrowUpOutlined, FileTextOutlined, AlertOutlined } from '@ant-design/icons'
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
  const [suppliers, setSuppliers] = useState([])
  const [selectedSuppliers, setSelectedSuppliers] = useState([])
  const [yearMonths, setYearMonths] = useState([])
  const [selectedYearMonths, setSelectedYearMonths] = useState([])
  const [aggregateType, setAggregateType] = useState('avg')

  const [trendData, setTrendData] = useState(null)
  const [gradeDistributionData, setGradeDistributionData] = useState(null)
  const [dimensionAvgData, setDimensionAvgData] = useState(null)

  const trendChartRef = useRef(null)
  const trendChartInstance = useRef(null)
  const gradeChartRef = useRef(null)
  const gradeChartInstance = useRef(null)
  const dimensionChartRef = useRef(null)
  const dimensionChartInstance = useRef(null)

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

  const fetchSuppliers = async () => {
    try {
      const res = await overviewService.getSuppliers()
      setSuppliers(res.data || [])
    } catch {
      console.error('Failed to fetch suppliers')
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
      if (selectedSuppliers.length > 0) {
        params.suppliers = selectedSuppliers
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

  const fetchGradeDistribution = async () => {
    try {
      const params = {}
      if (selectedCategories.length === 1) {
        params.category = selectedCategories[0]
      }
      const res = await overviewService.getGradeDistribution(params)
      setGradeDistributionData(res.data)
    } catch {
      console.error('Failed to fetch grade distribution')
    }
  }

  const fetchDimensionAvg = async () => {
    try {
      const params = {}
      if (selectedYearMonths.length > 0) {
        params.yearMonths = selectedYearMonths
      }
      const res = await overviewService.getDimensionAvg(params)
      setDimensionAvgData(res.data)
    } catch {
      console.error('Failed to fetch dimension avg')
    }
  }

  useEffect(() => {
    fetchStats()
    fetchCategories()
    fetchSuppliers()
    fetchYearMonths()
  }, [])

  useEffect(() => {
    fetchTrendData()
    fetchGradeDistribution()
    fetchDimensionAvg()
  }, [selectedCategories, selectedSuppliers, selectedYearMonths, aggregateType])

  useEffect(() => {
    if (!trendChartRef.current || !trendData) return

    if (!trendChartInstance.current) {
      trendChartInstance.current = echarts.init(trendChartRef.current)
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

    trendChartInstance.current.setOption(option)

    const handleResize = () => {
      trendChartInstance.current?.resize()
    }
    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [trendData, aggregateType])

  useEffect(() => {
    if (!gradeChartRef.current || !gradeDistributionData) return

    if (!gradeChartInstance.current) {
      gradeChartInstance.current = echarts.init(gradeChartRef.current)
    }

    const xAxisData = gradeDistributionData.map((item) => item.monthLabel)
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      legend: {
        data: ['A级', 'B级', 'C级', 'D级'],
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
        data: xAxisData,
      },
      yAxis: {
        type: 'value',
        name: '数量',
      },
      series: [
        {
          name: 'A级',
          type: 'bar',
          data: gradeDistributionData.map((item) => item.gradeA),
          itemStyle: { color: '#52c41a' },
        },
        {
          name: 'B级',
          type: 'bar',
          data: gradeDistributionData.map((item) => item.gradeB),
          itemStyle: { color: '#1890ff' },
        },
        {
          name: 'C级',
          type: 'bar',
          data: gradeDistributionData.map((item) => item.gradeC),
          itemStyle: { color: '#faad14' },
        },
        {
          name: 'D级',
          type: 'bar',
          data: gradeDistributionData.map((item) => item.gradeD),
          itemStyle: { color: '#ff4d4f' },
        },
      ],
    }

    gradeChartInstance.current.setOption(option)

    const handleResize = () => {
      gradeChartInstance.current?.resize()
    }
    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [gradeDistributionData])

  useEffect(() => {
    if (!dimensionChartRef.current || !dimensionAvgData) return

    if (!dimensionChartInstance.current) {
      dimensionChartInstance.current = echarts.init(dimensionChartRef.current)
    }

    const xAxisData = dimensionAvgData.map((item) => item.monthLabel)
    const option = {
      tooltip: {
        trigger: 'axis',
        axisPointer: { type: 'shadow' },
      },
      legend: {
        data: ['品质考核', '成本考核', '交货考核', '服务考核'],
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
        data: xAxisData,
      },
      yAxis: {
        type: 'value',
        name: '平均分',
      },
      series: [
        {
          name: '品质考核',
          type: 'bar',
          data: dimensionAvgData.map((item) => item.dimensionA),
          itemStyle: { color: '#1890ff' },
        },
        {
          name: '成本考核',
          type: 'bar',
          data: dimensionAvgData.map((item) => item.dimensionB),
          itemStyle: { color: '#52c41a' },
        },
        {
          name: '交货考核',
          type: 'bar',
          data: dimensionAvgData.map((item) => item.dimensionC),
          itemStyle: { color: '#faad14' },
        },
        {
          name: '服务考核',
          type: 'bar',
          data: dimensionAvgData.map((item) => item.dimensionD),
          itemStyle: { color: '#722ed1' },
        },
      ],
    }

    dimensionChartInstance.current.setOption(option)

    const handleResize = () => {
      dimensionChartInstance.current?.resize()
    }
    window.addEventListener('resize', handleResize)

    return () => {
      window.removeEventListener('resize', handleResize)
    }
  }, [dimensionAvgData])

  const handleCategoryChange = (values) => {
    setSelectedCategories(values)
  }

  const handleYearMonthChange = (values) => {
    setSelectedYearMonths(values)
  }

  const handleSupplierChange = (values) => {
    setSelectedSuppliers(values)
  }

  const handleAggregateTypeChange = (value) => {
    setAggregateType(value)
  }

  const gradeTableColumns = [
    { title: '月份', dataIndex: 'monthLabel', key: 'monthLabel' },
    { title: '供应商总数', dataIndex: 'totalCount', key: 'totalCount' },
    {
      title: 'A级',
      key: 'gradeA',
      render: (_, record) => (
        <span>
          <span>{record.gradeA}</span>
          <span style={{ color: '#333', marginLeft: '8px' }}>({record.gradeAPercent})</span>
        </span>
      ),
    },
    {
      title: 'B级',
      key: 'gradeB',
      render: (_, record) => (
        <span>
          <span>{record.gradeB}</span>
          <span style={{ color: '#333', marginLeft: '8px' }}>({record.gradeBPercent})</span>
        </span>
      ),
    },
    {
      title: 'C级',
      key: 'gradeC',
      render: (_, record) => (
        <span>
          <span>{record.gradeC}</span>
          <span style={{ color: '#333', marginLeft: '8px' }}>({record.gradeCPercent})</span>
        </span>
      ),
    },
    {
      title: 'D级',
      key: 'gradeD',
      render: (_, record) => (
        <span>
          <span>{record.gradeD}</span>
          <span style={{ color: '#333', marginLeft: '8px' }}>({record.gradeDPercent})</span>
        </span>
      ),
    },
  ]

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
            <Statistic title="启用供应商" value={stats.activeSupplierCount} prefix={<ArrowUpOutlined />} />
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
            <Col span={6}>
              <div style={{ fontWeight: 500, marginBottom: '8px' }}>类别筛选</div>
              <CheckboxGroup
                options={categories.map((c) => ({ label: c, value: c }))}
                value={selectedCategories}
                onChange={handleCategoryChange}
              />
            </Col>
            <Col span={6}>
              <div style={{ fontWeight: 500, marginBottom: '8px' }}>供应商筛选</div>
              <Select
                mode="multiple"
                placeholder="选择供应商（默认全部）"
                value={selectedSuppliers}
                onChange={handleSupplierChange}
                style={{ width: '100%' }}
                maxTagCount="responsive"
              >
                {suppliers.map((supplier) => (
                  <Option key={supplier} value={supplier}>
                    {supplier}
                  </Option>
                ))}
              </Select>
            </Col>
            <Col span={6}>
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
            <Col span={6}>
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
        <div ref={trendChartRef} style={{ height: '400px' }} />
      </Card>

      <Card style={{ marginTop: '24px' }}>
        <h3 style={{ marginBottom: '16px' }}>等级分布</h3>
        <div ref={gradeChartRef} style={{ height: '300px' }} />
        <div style={{ marginTop: '24px' }}>
          <Table
            columns={gradeTableColumns}
            dataSource={gradeDistributionData}
            rowKey="yearMonth"
            bordered
            pagination={false}
          />
        </div>
      </Card>

      <Card style={{ marginTop: '24px' }}>
        <h3 style={{ marginBottom: '16px' }}>各维度平均分对比</h3>
        <div ref={dimensionChartRef} style={{ height: '350px' }} />
      </Card>
    </div>
  )
}
