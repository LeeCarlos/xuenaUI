import { useState, useEffect, useRef } from 'react'
import { Card, Row, Col, Select, Button, message } from 'antd'
import * as echarts from 'echarts'
import compareService from '../services/compare'

const { Option } = Select

export default function Compare() {
  const [suppliers, setSuppliers] = useState([])
  const [categories, setCategories] = useState([])
  const [selectedSuppliers, setSelectedSuppliers] = useState([])
  const [selectedCategory, setSelectedCategory] = useState('')
  const [yearMonth, setYearMonth] = useState('')
  const [radarData, setRadarData] = useState(null)
  const [heatmapData, setHeatmapData] = useState(null)

  const radarChartRef = useRef(null)
  const heatmapChartRef = useRef(null)
  const radarChartInstance = useRef(null)
  const heatmapChartInstance = useRef(null)

  const fetchSuppliers = async () => {
    try {
      const res = await compareService.getSuppliers()
      setSuppliers(res.data || [])
    } catch {
      message.error('获取供应商列表失败')
    }
  }

  const fetchCategories = async () => {
    try {
      const res = await compareService.getCategories()
      setCategories(res.data || [])
    } catch {
      message.error('获取类别列表失败')
    }
  }

  const fetchRadarData = async () => {
    if (selectedSuppliers.length === 0) return
    try {
      const params = { suppliers: selectedSuppliers }
      if (yearMonth) params.yearMonth = yearMonth
      const res = await compareService.getRadar(params)
      setRadarData(res.data)
    } catch {
      message.error('获取雷达图数据失败')
    }
  }

  const fetchHeatmapData = async () => {
    if (!selectedCategory) return
    try {
      const params = { category: selectedCategory }
      if (yearMonth) params.yearMonth = yearMonth
      const res = await compareService.getHeatmap(params)
      setHeatmapData(res.data)
    } catch {
      message.error('获取热力图数据失败')
    }
  }

  useEffect(() => {
    fetchSuppliers()
    fetchCategories()
  }, [])

  useEffect(() => {
    fetchRadarData()
  }, [selectedSuppliers, yearMonth])

  useEffect(() => {
    fetchHeatmapData()
  }, [selectedCategory, yearMonth])

  useEffect(() => {
    if (!radarChartRef.current || !radarData) return

    if (!radarChartInstance.current) {
      radarChartInstance.current = echarts.init(radarChartRef.current)
    }

    const option = {
      tooltip: {},
      legend: {
        data: radarData.suppliers.map(s => s.name),
        bottom: 10,
      },
      radar: {
        indicator: radarData.indicators.map(name => ({ name, max: 100 })),
      },
      series: [{
        type: 'radar',
        data: radarData.suppliers.map(s => ({
          value: s.values,
          name: s.name,
        })),
      }],
    }

    radarChartInstance.current.setOption(option)

    const handleResize = () => radarChartInstance.current?.resize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [radarData])

  useEffect(() => {
    if (!heatmapChartRef.current || !heatmapData) return

    if (!heatmapChartInstance.current) {
      heatmapChartInstance.current = echarts.init(heatmapChartRef.current)
    }

    const option = {
      tooltip: {
        position: 'top',
      },
      grid: {
        left: '15%',
        right: '5%',
        top: '10%',
        bottom: '15%',
      },
      xAxis: {
        type: 'category',
        data: heatmapData.columns,
        splitArea: { show: true },
      },
      yAxis: {
        type: 'category',
        data: heatmapData.rows,
        splitArea: { show: true },
      },
      visualMap: {
        min: 0,
        max: 100,
        calculable: true,
        orient: 'horizontal',
        left: 'center',
        bottom: '5%',
      },
      series: [{
        name: '得分',
        type: 'heatmap',
        data: heatmapData.values.flatMap((row, i) =>
          row.map((value, j) => [j, i, value])
        ),
        label: { show: true },
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowColor: 'rgba(0, 0, 0, 0.5)',
          },
        },
      }],
    }

    heatmapChartInstance.current.setOption(option)

    const handleResize = () => heatmapChartInstance.current?.resize()
    window.addEventListener('resize', handleResize)
    return () => window.removeEventListener('resize', handleResize)
  }, [heatmapData])

  return (
    <div>
      <h2 style={{ marginBottom: '24px' }}>差异分析</h2>

      <Row gutter={[16, 16]}>
        <Col span={12}>
          <Card title="供应商对比（雷达图）">
            <Row gutter={[16, 16]} style={{ marginBottom: '16px' }}>
              <Col span={12}>
                <Select
                  mode="multiple"
                  placeholder="选择供应商"
                  value={selectedSuppliers}
                  onChange={setSelectedSuppliers}
                  style={{ width: '100%' }}
                  maxTagCount="responsive"
                >
                  {suppliers.map(s => (
                    <Option key={s} value={s}>{s}</Option>
                  ))}
                </Select>
              </Col>
              <Col span={12}>
                <Select
                  placeholder="选择月份"
                  value={yearMonth}
                  onChange={setYearMonth}
                  style={{ width: '100%' }}
                  allowClear
                >
                  <Option value="">全部月份</Option>
                </Select>
              </Col>
            </Row>
            <div ref={radarChartRef} style={{ height: '400px' }} />
          </Card>
        </Col>

        <Col span={12}>
          <Card title="类别供应商得分对比（热力图）">
            <Row gutter={[16, 16]} style={{ marginBottom: '16px' }}>
              <Col span={12}>
                <Select
                  placeholder="选择类别"
                  value={selectedCategory}
                  onChange={setSelectedCategory}
                  style={{ width: '100%' }}
                  allowClear
                >
                  {categories.map(c => (
                    <Option key={c} value={c}>{c}</Option>
                  ))}
                </Select>
              </Col>
              <Col span={12}>
                <Select
                  placeholder="选择月份"
                  value={yearMonth}
                  onChange={setYearMonth}
                  style={{ width: '100%' }}
                  allowClear
                >
                  <Option value="">全部月份</Option>
                </Select>
              </Col>
            </Row>
            <div ref={heatmapChartRef} style={{ height: '400px' }} />
          </Card>
        </Col>
      </Row>
    </div>
  )
}
