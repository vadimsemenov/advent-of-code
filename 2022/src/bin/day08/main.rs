fn part1(input: &str) -> u64 {
    let forest: Vec<Vec<u8>> = input.lines().map(Vec::from).collect();
    let mut ans = 0;
    for i in 0..forest.len() {
        for j in 0..forest[i].len() {
            for (di, dj) in DIR {
                let mut visible = true;
                let mut ii = i.wrapping_add(di);
                let mut jj = j.wrapping_add(dj);
                while ii < forest.len() && jj < forest[ii].len() {
                    visible &= forest[ii][jj] < forest[i][j];
                    ii = ii.wrapping_add(di);
                    jj = jj.wrapping_add(dj);
                }
                if visible {
                    ans += 1;
                    break;
                }
            }
        }
    }
    ans
}

fn part2(input: &str) -> u64 {
    let forest: Vec<Vec<u8>> = input.lines().map(Vec::from).collect();
    let mut ans = 0;
    for i in 0..forest.len() {
        for j in 0..forest[i].len() {
            let mut score = 1;
            for (di, dj) in DIR {
                let mut ii = i.wrapping_add(di);
                let mut jj = j.wrapping_add(dj);
                let mut count = 0;
                while ii < forest.len() && jj < forest[ii].len() {
                    count += 1;
                    if forest[ii][jj] >= forest[i][j] {
                        break;
                    }
                    ii = ii.wrapping_add(di);
                    jj = jj.wrapping_add(dj);
                }
                score *= count;
            }
            if ans < score {
                ans = score;
            }
        }
    }
    ans
}

const DIR: [(usize, usize); 4] = [(0, 1), (usize::MAX, 0), (0, usize::MAX), (1, 0)];

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
        assert_eq!(part1(SAMPLE), 21);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 1818);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 8);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 368368);
    }
}
