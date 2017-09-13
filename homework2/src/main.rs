#[macro_use]
extern crate serde_derive;

extern crate serde;
extern crate serde_json;

use std::fs::File;
use std::io::prelude::*;
use std::io::Read;
use std::ops::Add;
use serde_json::Error;

#[derive(Serialize, Deserialize)]
struct Corpus {
    documents: Vec<Document>,
}

#[derive(Serialize, Deserialize)]
struct Document {
    title: String,
    body: String,
    url: String,
}

fn main() {
    let mut file = File::open("all-nps-sites.json").unwrap();
    let mut contents = String::new();
    file.read_to_string(&mut contents);

    let documents: Corpus = serde_json::from_str(&contents).unwrap();

    for (i, document) in documents.documents.iter().enumerate() {
        use std::error::Error;

        let file_name = format!("article{}.json", i);

        let mut file = match File::create(&file_name) {
            Err(why) => panic!("Couldn't create {}: {}", &file_name, why.description()),
            Ok(file) => file,
        };

        let json = serde_json::to_string(&document).unwrap();
        file.write(&json.as_bytes());
    }
}
