CREATE TABLE resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    file_id BIGINT,
    subsection_id BIGINT,
    FOREIGN KEY (file_id) REFERENCES files(id),
    FOREIGN KEY (subsection_id) REFERENCES course_subsections(id)
);

