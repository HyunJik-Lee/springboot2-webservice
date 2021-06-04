package org.example.service.posts;

import lombok.RequiredArgsConstructor;
import org.example.domain.posts.Posts;
import org.example.domain.posts.PostsRepository;
import org.example.web.dto.PostsResponseDto;
import org.example.web.dto.PostsSaveRequestDto;
import org.example.web.dto.PostsUpdateRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto){
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        // JPA의 영속성 컨텍스트 때문에 쿼리를 날리는 부분이 없다.
        // 영속성 컨텍스트란, 엔티티를 영구 저장하는 논리적 환경
        // 핵심은, 엔티티가 영속성 컨텍스트에 포함되어 있냐 아니냐
        // JPA의 EntityManager가 활성화된 상태로 트랜잭션 안에서 데이터베이스에서 데이터를 가져오면 이 데이터는 영속성 컨텍스트가 유지된다.
        // 이 상태에서 해당 데이터 값을 변경하면 트랜잭션이 끝나는 시점에 해당 테이블에 변경분을 반영
        // 이 개념을 더티 체킹이라고 한다.
        posts.update(requestDto.getTitle(), requestDto.getContent());
        return id;
    }

    @Transactional
    public PostsResponseDto findById (Long id) {
        Posts entity = postsRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 게시글이 없습니다. id="+ id));
        return new PostsResponseDto(entity);
    }
}
