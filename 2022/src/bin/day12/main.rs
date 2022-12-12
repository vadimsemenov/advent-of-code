use std::collections::{HashSet, VecDeque};

fn part1(input: &str) -> usize {
    solve(input, &HashSet::from([b'S']))
}

fn part2(input: &str) -> usize {
    solve(input, &HashSet::from([b'S', b'a']))
}

fn solve(input: &str, valid_start: &HashSet<u8>) -> usize {
    let mut grid: Vec<Vec<u8>> = input.lines().map(|s| s.bytes().collect()).collect();
    let mut queue = VecDeque::new();
    let mut target = HashSet::new();
    let mut distance = vec![Vec::new(); grid.len()];
    for i in 0..grid.len() {
        distance[i].resize(grid[i].len(), usize::MAX);
        for j in 0..grid[i].len() {
            if valid_start.contains(&grid[i][j]) {
                queue.push_back((i, j));
                grid[i][j] = b'a';
                distance[i][j] = 0;
            } else if grid[i][j] == b'E' {
                target.insert((i, j));
                grid[i][j] = b'z';
            }
        }
    }

    let dir = [(1, 0), (0, 1), (0, usize::MAX), (usize::MAX, 0)];
    while !queue.is_empty() {
        let (x, y) = queue.pop_front().unwrap();
        if target.contains(&(x, y)) {
            return distance[x][y];
        }
        for (dx, dy) in dir {
            let xx = x.wrapping_add(dx);
            let yy = y.wrapping_add(dy);
            if xx < grid.len() && yy < grid[xx].len() && distance[xx][yy] == usize::MAX {
                if grid[xx][yy] <= grid[x][y] + 1 {
                    distance[xx][yy] = distance[x][y] + 1;
                    queue.push_back((xx, yy));
                }
            }
        }
    }
    panic!("unreachable")
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
        assert_eq!(part1(SAMPLE), 31);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 484);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 29);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 478);
    }
}
