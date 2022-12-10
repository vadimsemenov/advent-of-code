fn part1(input: &str) -> i32 {
    solve(input, Box::new(Part1::default()))
}

fn part2(input: &str) -> String {
    solve(input, Box::new(Part2::default()))
}

fn solve<T>(input: &str, mut cpu: Box<dyn CPU<T>>) -> T {
    for line in input.lines() {
        if line.trim() == "noop" {
            cpu.noop();
        } else {
            let (cmd, delta) = line.split_once(' ').unwrap();
            assert_eq!(cmd, "addx");
            let delta = delta.parse().unwrap();
            cpu.addx(delta);
        }
    }
    cpu.ans()
}

trait CPU<T> {
    fn noop(&mut self);
    fn addx(&mut self, delta: i32);
    fn ans(&self) -> T;
}

struct Part1 {
    cur: i32,
    x: i32,
    ans: i32,
}

impl Default for Part1 {
    fn default() -> Self {
        Self {
            cur: 0,
            x: 1,
            ans: 0,
        }
    }
}

impl CPU<i32> for Part1 {
    fn noop(&mut self) {
        self.cur += 1;
        if self.cur % 40 == 20 {
            self.ans += self.cur * self.x;
        }
    }

    fn addx(&mut self, delta: i32) {
        self.noop();
        self.noop();
        self.x += delta;
    }

    fn ans(&self) -> i32 {
        self.ans
    }
}

struct Part2 {
    cur: usize,
    x: usize,
    crt: Vec<Vec<char>>,
}

impl Default for Part2 {
    fn default() -> Self {
        Self {
            cur: 0,
            x: 1,
            crt: vec![vec!['.'; 40]; 6],
        }
    }
}

impl CPU<String> for Part2 {
    fn noop(&mut self) {
        let row = self.cur / 40;
        let col = self.cur % 40;
        let mut sprite = self.x.wrapping_sub(1);
        while sprite != self.x.wrapping_add(2) {
            if sprite == col {
                self.crt[row][col] = '#'
            }
            sprite = sprite.wrapping_add(1);
        }
        self.cur += 1;
    }

    fn addx(&mut self, delta: i32) {
        self.noop();
        self.noop();
        self.x = self.x.wrapping_add(delta as usize);
    }

    fn ans(&self) -> String {
        self.crt
            .iter()
            .map(|row| row.iter().collect())
            .collect::<Vec<String>>()
            .join("\n")
    }
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
        assert_eq!(part1(SAMPLE), 13140);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 17020);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(
            part2(SAMPLE),
            "\
##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######....."
        );
    }

    #[test]
    fn test_part_2() {
        assert_eq!(
            part2(INPUT),
            "\
###..#....####.####.####.#.....##..####.
#..#.#....#.......#.#....#....#..#.#....
#..#.#....###....#..###..#....#....###..
###..#....#.....#...#....#....#.##.#....
#.#..#....#....#....#....#....#..#.#....
#..#.####.####.####.#....####..###.####."
        );
    }
}
