use std::collections::HashMap;

fn part1(input: &str) -> u64 {
    sizes(input)
        .into_values()
        .filter(|&size| size <= 100_000)
        .sum()
}

fn part2(input: &str) -> u64 {
    let sizes = sizes(input);

    let used_space = sizes["//"];
    let free_space = 70_000_000 - used_space;
    let need_space = 30_000_000 - free_space;

    sizes
        .into_values()
        .filter(|&size| size >= need_space)
        .min()
        .unwrap()
}

fn sizes(input: &str) -> HashMap<String, u64> {
    let mut stack = Vec::<u64>::new();
    let mut path = String::new();
    let mut sizes: HashMap<String, u64> = HashMap::new();

    fn complete_dir(path: &mut String, stack: &mut Vec<u64>, sizes: &mut HashMap<String, u64>) {
        let size = stack.pop().unwrap();
        if let Some(parent_size) = stack.last_mut() {
            *parent_size += size;
        };
        sizes.insert(path.clone(), size);

        let (parent, _) = path.rsplit_once('/').unwrap();
        *path = String::from(parent);
    }

    for line in input.lines() {
        if let Some(dest) = line.strip_prefix("$ cd ") {
            match dest {
                ".." => {
                    complete_dir(&mut path, &mut stack, &mut sizes);
                }
                _ => {
                    path += "/";
                    path += dest;
                    stack.push(0);
                }
            };
        } else if line.starts_with("$ ls") {
        } else {
            let (size, _) = line.split_once(' ').unwrap();
            if let Ok(size) = size.parse::<u64>() {
                *stack.last_mut().unwrap() += size;
            } // else 'dir'
        }
    }
    while !stack.is_empty() {
        complete_dir(&mut path, &mut stack, &mut sizes);
    }
    sizes
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
        assert_eq!(part1(SAMPLE), 95437);
    }

    #[test]
    fn test_part_1() {
        assert_eq!(part1(INPUT), 1778099);
    }

    #[test]
    fn test_part_2_sample() {
        assert_eq!(part2(SAMPLE), 24933642);
    }

    #[test]
    fn test_part_2() {
        assert_eq!(part2(INPUT), 1623571);
    }
}
