package by.av.test.testavby.service;

import by.av.test.testavby.repository.ImageModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ImageModelService {
    private final ImageModelRepository imageModelRepository;

    @Autowired
    public ImageModelService(ImageModelRepository imageModelRepository) {
        this.imageModelRepository = imageModelRepository;
    }
}
