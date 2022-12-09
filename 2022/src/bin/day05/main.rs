fn part1(input: &str) -> String {
    let (mut stacks, ops) = parse(input);
    for (count, from, to) in ops {
        for _ in 0..count {
            let c = stacks[from].pop().unwrap();
            stacks[to].push(c);
        }
    }

    stacks.iter().filter_map(|stack| stack.last()).collect()
}

fn part2(input: &str) -> String {
    let (mut stacks, ops) = parse(input);

    for (count, from, to) in ops {
        let from_len = stacks[from].len();
        let moved = stacks[from].split_off(from_len - count);
        stacks[to].extend(moved);
    }

    stacks.iter().filter_map(|stack| stack.last()).collect()
}

type Input = (Vec<Vec<char>>, Vec<Op>);
type Op = (usize, usize, usize);

fn parse(input: &str) -> Input {
    let (crates, commands) = input.split_once("\n\n").unwrap();

    let crates: Vec<&str> = crates.lines().collect();
    let (&indices, crates) = crates.split_last().unwrap();
    let crates: Vec<Vec<char>> = crates.iter().map(|&s| s.chars().collect()).collect();
    let stacks_qty: usize = String::from(indices)
        .trim()
        .split_whitespace()
        .flat_map(str::parse)
        .last()
        .unwrap();
    let mut stacks = Vec::with_capacity(stacks_qty);
    for i in 0..stacks_qty {
        stacks.push(Vec::new());
        let idx = i * 4 + 1;
        for j in (0..crates.len()).rev() {
            if let Some(&c) = crates[j].get(idx) {
                if c != ' ' {
                    stacks[i].push(c);
                }
            }
        }
    }

    let mut ops: Vec<Op> = Vec::new();
    for line in commands.lines() {
        if let &[count, from, to] = line
            .split(' ')
            .flat_map(str::parse)
            .collect::<Vec<_>>()
            .as_slice()
        {
            ops.push((count, from - 1, to - 1));
        } else {
            panic!();
        }
    }

    (stacks, ops)
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
        assert_eq!(part1(SAMPLE), "CMZ");
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), "FWSHSPJWM");
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), "MCD");
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), "PWPWHGFZS");
    }
}
