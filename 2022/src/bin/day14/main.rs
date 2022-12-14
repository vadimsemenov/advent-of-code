use std::cmp::{max, min};
use std::collections::HashSet;

fn part1(input: &str) -> usize {
    let mut grid = parse_grid(input);
    let &(_, max_y) = grid.iter().max_by_key(|(_, y)| y).unwrap();
    solve(grid, |(_, y), _| y > max_y)
}

fn part2(input: &str) -> usize {
    let mut grid = parse_grid(input);
    let &(_, max_y) = grid.iter().max_by_key(|(_, y)| y).unwrap();
    let floor = max_y + 2;
    for x in -floor..=floor {
        grid.insert((500 + x, floor));
    }
    solve(grid, |position, grid| grid.contains(&position))
}

fn solve<F>(mut grid: HashSet<(isize, isize)>, enough: F) -> usize
where
    F: Fn((isize, isize), &HashSet<(isize, isize)>) -> bool,
{
    for count in 0.. {
        let mut cur = (500, 0);
        'step: loop {
            if enough(cur, &grid) {
                return count;
            }
            for (dx, dy) in [(0, 1), (-1, 1), (1, 1)] {
                let next = (cur.0 + dx, cur.1 + dy);
                if !grid.contains(&next) {
                    cur = next;
                    continue 'step;
                }
            }
            grid.insert(cur);
            break;
        }
    }
    unreachable!()
}

fn parse_grid(input: &str) -> HashSet<(isize, isize)> {
    let paths: Vec<Vec<_>> = input
        .split('\n')
        .map(|path| {
            path.split(" -> ")
                .map(|point| {
                    let (x, y) = point.split_once(',').unwrap();
                    (x.parse().unwrap(), y.parse().unwrap())
                })
                .collect()
        })
        .collect();
    let mut grid = HashSet::new();
    for path in paths {
        for i in 1..path.len() {
            let (ax, ay) = path[i - 1];
            let (bx, by) = path[i];
            for x in min(ax, bx)..=max(ax, bx) {
                for y in min(ay, by)..=max(ay, by) {
                    grid.insert((x, y));
                }
            }
        }
    }
    grid
}

const INPUT: &str = include_str!("input.txt");
fn main() {
    println!("{}", part1(INPUT));
    println!("{}", part2(INPUT));
}

#[cfg(test)]
mod tests {
    use super::*;
    const SAMPLE: &str = include_str!("sample.txt");

    #[test]
    fn test_part_1_sample() {
        assert_eq!(part1(SAMPLE), 24);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 674);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 93);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 24958);
    }
}
