package com.kt_miniproject.demo.service;

import com.kt_miniproject.demo.domain.book.Book;
import com.kt_miniproject.demo.domain.user.User;
import com.kt_miniproject.demo.dto.book.BookCreateRequest;
import com.kt_miniproject.demo.dto.book.BookResponse;
import com.kt_miniproject.demo.exception.ResourceNotFoundException;
import com.kt_miniproject.demo.repository.BookRepository;
import com.kt_miniproject.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service               // 서비스 빈 등록
@RequiredArgsConstructor
@Transactional(readOnly = true)

public class BookServiceImpl implements BookService {

    //AutoWired 대신 RequiredArgsConstructor로 의존성 주입
    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/images/generations";


    // 도서 등록
    @Override
    @Transactional
    public BookResponse createBook(BookCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = Book.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .coverImageUrl(request.getCoverImageUrl())
                .user(user)
                .build();

        Book saved = bookRepository.save(book);
        return new BookResponse(saved);
    }

    // 전체 조회
    @Override
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(BookResponse::new)
                .toList();
    }

    // 상세 조회
    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found. id=" + id)); //  illegalArgument에서 직접 처리한 예외로 변경
        return new BookResponse(book);
    }

    // 수정
    @Override
    @Transactional
    public BookResponse updateBook(Long id, BookCreateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found. id=" + id)); //  illegalArgument에서 직접 처리한 예외로 변경

        book.setTitle(request.getTitle());
        book.setContent(request.getContent());
        book.setCoverImageUrl(request.getCoverImageUrl());

        // 변경감지(dirty checking)로 자동 update
        return new BookResponse(book);
    }

    // 삭제
    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found. id=" + id); //  illegalArgument에서 직접 처리한 예외로 변경
        }
        bookRepository.deleteById(id);
    }

    // 제목 검색 (like 검색)
    @Override
    public List<BookResponse> searchBooks(String title) {
        if (title == null || title.isBlank()) {
            return getAllBooks();
        }

        return bookRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .map(BookResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public BookResponse updateBookCoverUrl(Long id, String imageUrl) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found. id=" + id)); //  illegalArgument에서 직접 처리한 예외로 변경
        book.setCoverImageUrl(imageUrl);

        // 변경감지(dirty checking)로 자동 update
        return new BookResponse(book);
    }

    @Override
    @Transactional
    public String generateAiImageUrl(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        String apiKey = book.getUser().getApiKey();
        String title = book.getTitle();
        String content = book.getContent();

        // 2. API Key 가져오기
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API Key가 등록되지 않았습니다.");
        }

        // 내용이 너무 길면 DALL-E 요청 제한에 걸릴 수 있으므로 500자 정도로 자름
        String summary = (content != null && content.length() > 500)
                ? content.substring(0, 500)
                : content;

        // "제목이 ~이고, 내용이 ~인 책의 표지를 그려줘"
        String prompt = String.format(
                "A high-quality book cover illustration for a book titled '%s'. The story is about: %s",
                title, summary
        );

        // 4. 헤더 설정 (인증)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // 5. 요청 바디 설정 (DALL-E 3 파라미터)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "dall-e-3");
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);           // 1장 생성
        requestBody.put("size", "1024x1024");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // 6. API 호출 (POST)
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_API_URL, entity, Map.class);

            // 7. 응답에서 URL 꺼내기
            // JSON 구조: { "data": [ { "url": "https://..." } ] }
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("data")) {
                throw new IllegalStateException("이미지 생성 실패: 응답 데이터 없음");
            }

            List<Map<String, String>> data = (List<Map<String, String>>) body.get("data");
            String generatedImageUrl = data.get(0).get("url");

            // 생성된 이미지를 바로 책 정보에 저장
            book.setCoverImageUrl(generatedImageUrl);

            return generatedImageUrl;

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("OpenAI API 호출 실패: " + e.getMessage());
        }
    }

}
