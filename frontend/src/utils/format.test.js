import { describe, expect, it } from 'vitest'
import { formatCurrency, formatDate, formatStatus } from './format'

describe('format utilities', () => {
  it('formats currency values with a stable USD display', () => {
    expect(formatCurrency(42)).toBe('$42.00')
    expect(formatCurrency('19.5')).toBe('$19.50')
    expect(formatCurrency(null)).toBe('$0.00')
  })

  it('formats status values for admin badges and tables', () => {
    expect(formatStatus('PENDING')).toBe('Pending')
    expect(formatStatus('IN_PROGRESS')).toBe('In Progress')
    expect(formatStatus(null)).toBe('Unknown')
  })

  it('handles missing dates with a friendly fallback', () => {
    expect(formatDate(null)).toBe('Not available')
  })
})
