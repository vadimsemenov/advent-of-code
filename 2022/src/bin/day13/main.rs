use crate::Token::{List, Single};
use std::cmp::{min, Ordering};

fn part1(input: &str) -> usize {
    let mut ans = 0;
    for (idx, pair) in input.split("\n\n").enumerate() {
        let (fst, snd) = pair.split_once('\n').unwrap();
        let (fst, snd) = (parse(fst), parse(snd));
        if fst <= snd {
            ans += idx + 1;
        }
    }
    ans
}

fn part2(input: &str) -> usize {
    let mut vec: Vec<Token> = input
        .lines()
        .filter(|&l| !l.is_empty())
        .map(parse)
        .collect();

    let needles = vec![
        List(vec![List(vec![Single(2)])]),
        List(vec![List(vec![Single(6)])]),
    ];

    vec.append(&mut needles.clone());
    vec.sort();

    let mut ans = 1;
    for i in 0..vec.len() {
        if needles.contains(&vec[i]) {
            ans *= i + 1;
        }
    }
    ans
}

fn parse(string: &str) -> Token {
    let mut stack = vec![vec![]];
    let mut last_num = None;
    for c in string.bytes() {
        match c {
            b'[' => stack.push(vec![]),
            b']' | b',' => {
                if let Some(num) = last_num {
                    stack.last_mut().unwrap().push(Single(num));
                    last_num = None;
                }
                if c == b']' {
                    let list = List(stack.pop().unwrap());
                    stack.last_mut().unwrap().push(list);
                }
            }
            b'0'..=b'9' => {
                let digit = (c - b'0').into();
                last_num = Some(match last_num {
                    Some(num) => num * 10 + digit,
                    None => digit,
                });
            }
            _ => panic!("Unexpected '{}'", c as char),
        }
    }
    assert_eq!(stack.len(), 1);
    let mut container = stack.pop().unwrap();
    assert_eq!(container.len(), 1);
    container.pop().unwrap()
}

#[derive(Debug, Clone, PartialEq, Eq)]
enum Token {
    Single(u32),
    List(Vec<Token>),
}

impl PartialOrd<Self> for Token {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl Ord for Token {
    fn cmp(&self, other: &Self) -> Ordering {
        match (self, other) {
            (Single(fst), Single(snd)) => fst.cmp(snd),
            (List(fst), List(snd)) => {
                for i in 0..min(fst.len(), snd.len()) {
                    let c = fst[i].cmp(&snd[i]);
                    if c != Ordering::Equal {
                        return c;
                    }
                }
                return fst.len().cmp(&snd.len());
            }
            (Single(fst), List(_)) => List(vec![Single(*fst)]).cmp(other),
            (List(_), Single(snd)) => self.cmp(&List(vec![Single(*snd)])),
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
        assert_eq!(part1(SAMPLE), 13);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 5905);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 140);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 21691);
    }
}
