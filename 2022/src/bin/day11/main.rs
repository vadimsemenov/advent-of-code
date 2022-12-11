use crate::Op::Square;
use std::collections::VecDeque;

fn part1(input: &str) -> usize {
    solve(input, 20, 3)
}

fn part2(input: &str) -> usize {
    solve(input, 10000, 1)
}

fn solve(input: &str, rounds: usize, relief: i64) -> usize {
    let mut monkeys = parse(input);
    let qty = monkeys.len();
    let mut counts = vec![0; qty];
    let modulo: i64 = monkeys.iter().map(|m| m.test).product();

    for _round in 0..rounds {
        for i in 0..qty {
            counts[i] += monkeys[i].items.len();
            while let Some(old) = monkeys[i].items.pop_front() {
                let mut new = monkeys[i].operation.apply(old) % modulo;
                new /= relief;
                let throw_to = monkeys[i].throw_to;
                let throw_to = if new % monkeys[i].test == 0 {
                    throw_to.0
                } else {
                    throw_to.1
                };
                monkeys[throw_to].items.push_back(new);
            }
        }
    }

    counts.sort();
    counts.pop().unwrap() * counts.pop().unwrap()
}

fn parse(input: &str) -> Vec<Monkey> {
    let mut monkeys = Vec::new();
    for monkey in input.split("\n\n") {
        let mut iter = monkey.lines();
        assert!(iter.next().unwrap().starts_with("Monkey "));
        let starting_items = iter
            .next()
            .unwrap()
            .strip_prefix("  Starting items: ")
            .unwrap()
            .split(", ")
            .flat_map(str::parse)
            .collect();
        let (op, operand) = iter
            .next()
            .unwrap()
            .strip_prefix("  Operation: new = old ")
            .unwrap()
            .split_once(' ')
            .unwrap();
        let operand = operand.parse().ok();
        let operation = match op {
            "*" => {
                if let Some(other) = operand {
                    Op::Mul(other)
                } else {
                    Square
                }
            }
            "+" => Op::Add(operand.unwrap()),
            _ => panic!(),
        };
        let test = iter
            .next()
            .unwrap()
            .strip_prefix("  Test: divisible by ")
            .unwrap()
            .parse()
            .unwrap();
        let if_true = iter
            .next()
            .unwrap()
            .strip_prefix("    If true: throw to monkey ")
            .unwrap()
            .parse()
            .unwrap();
        let if_false = iter
            .next()
            .unwrap()
            .strip_prefix("    If false: throw to monkey ")
            .unwrap()
            .parse()
            .unwrap();
        assert_eq!(iter.next(), None);
        monkeys.push(Monkey {
            items: starting_items,
            operation,
            test,
            throw_to: (if_true, if_false),
        });
    }
    monkeys
}

struct Monkey {
    items: VecDeque<i64>,
    operation: Op,
    test: i64,
    throw_to: (usize, usize),
}

enum Op {
    Add(i64),
    Mul(i64),
    Square,
}

impl Op {
    fn apply(&self, old: i64) -> i64 {
        match self {
            Op::Mul(other) => old * other,
            Op::Add(other) => old + other,
            Op::Square => old * old,
        }
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
        assert_eq!(part1(SAMPLE), 10605);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 107822);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 2713310158);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 27267163742);
    }
}
