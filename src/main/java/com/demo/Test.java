package com.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class Test {
    public static class Source {
        Long id;
        String name;
        String email;
        String delyn;

        public Source(Long id, String name, String email, String delyn) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.delyn = delyn;
        }

        public Long getId() { return id; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getDelyn() { return delyn; }
    }

    public static class Target {
        Long id;
        String name;
        String email;
        public Target(Long id) {
            this.id = id;
        }

        public Long getId() { return id; }
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }

        @Override
        public String toString() {
            return "Target{id=" + id + ", name='" + name + "', email='" + email + "'}";
        }
    }


    public static void mergeById(List<Source> sourceList, List<Target> targetList) {

        // Map<id, Source> 생성

        Map<Long, Source> sourceMap = sourceList.stream()

                .collect(Collectors.toMap(Source::getId, s -> s));

        // 타겟 리스트 돌면서 매핑
        for (Target t : targetList) {
            Source s = sourceMap.get(t.getId());
            if (s != null) {
                t.setName(s.getName());
                t.setEmail(s.getEmail());
            }
        }

    }

    public static void main(String[] args) {8

        Source[] arrSource = {
                new Source(2L, "홍길동", "hong@example.com",  "Y"),
                new Source(3L, "박진철", "hong@example.com",  "N")
        };

        List<Source> sourceList = Arrays.asList(arrSource);

        List<Target> targetList = Arrays.asList(
                new Target(2L),
                new Target(3L),
                new Target(4L)
        );

        mergeById(sourceList.stream().filter(item-> !"Y".equals(item.getDelyn())).collect(Collectors.toList()), targetList);

        targetList.forEach(System.out::println);

        

    }

}
