package by.av.test.testavby.mapper.post;

import by.av.test.testavby.dto.PostDTO;
import by.av.test.testavby.entity.Post;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public PostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Post convertPostDTOToPost(PostDTO postDTO) {
        return modelMapper.map(postDTO, Post.class);
    }

    public PostDTO convertPostToPostDTO(Post post) {
        return modelMapper.map(post, PostDTO.class);
    }
}
