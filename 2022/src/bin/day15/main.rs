use std::cmp::max;
use std::collections::HashSet;

fn part1(input: &str, y: i64) -> usize {
    let sensors = parse_sensors(input);

    let mut cannot = HashSet::new();
    for (sensor, beacon) in &sensors {
        let radius = dist(sensor, beacon);
        let spread = radius - (sensor.1 - y).abs();
        if spread < 0 {
            continue;
        }
        for x in sensor.0 - spread..=sensor.0 + spread {
            cannot.insert(x);
        }
    }
    for (sensor, beacon) in &sensors {
        if beacon.1 == y {
            cannot.remove(&beacon.0);
        }
        if sensor.1 == y {
            cannot.remove(&sensor.0);
        }
    }

    return cannot.len();
}

fn part2(input: &str, limit: i64) -> i64 {
    let mut sensors = parse_sensors(input);

    sensors.sort_by_key(|&(sensor, _)| sensor);
    for y in 0..=limit {
        let mut x = 0;
        for (sensor, beacon) in &sensors {
            let radius = dist(&sensor, &beacon);
            let spread = radius - (sensor.1 - y).abs();
            if spread < 0 {
                continue;
            }
            if sensor.0 - spread <= x {
                x = max(x, sensor.0 + spread + 1);
            }
        }
        if x <= limit {
            return x * 4_000_000 + y;
        }
    }
    unreachable!();
}

fn parse_sensors(input: &str) -> Vec<(Point, Point)> {
    let sensors: Vec<(Point, Point)> = input
        .lines()
        .map(|line| {
            let (sensor, beacon) = line
                .strip_prefix("Sensor at x=")
                .unwrap()
                .split_once(": closest beacon is at x=")
                .unwrap();
            let (sx, sy) = sensor.split_once(", y=").unwrap();
            let (bx, by) = beacon.split_once(", y=").unwrap();
            let sensor = (sx.parse().unwrap(), sy.parse().unwrap());
            let beacon = (bx.parse().unwrap(), by.parse().unwrap());
            (sensor, beacon)
        })
        .collect();
    sensors
}

type Point = (i64, i64);

fn dist(x: &Point, y: &Point) -> i64 {
    (x.0 - y.0).abs() + (x.1 - y.1).abs()
}

const INPUT: &str = include_str!("input.txt");
fn main() {
    println!("{}", part1(INPUT, 2_000_000));
    println!("{}", part2(INPUT, 4_000_000));
}

#[cfg(test)]
mod tests {
    use super::*;
    const SAMPLE: &str = include_str!("sample.txt");

    #[test]
    fn test_part_1_sample() {
        assert_eq!(part1(SAMPLE, 10), 26);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT, 2000000), 4886370);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE, 20), 56000011);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT, 4_000_000), 11374534948438);
    }
}
