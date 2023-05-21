import {Component, OnInit} from '@angular/core';
import {TransportService} from "../../service/transport.service";
import {TransportBrand} from "../../models/transport/TransportBrand";
import {TransportModel} from "../../models/transport/TransportModel";
import {GenerationTransport} from "../../models/transport/GenerationTransport";
import {ImageUploadService} from "../../service/image-upload.service";
import {TransportParameters} from "../../models/transport/TransportParameters";

const PAGE = 0;
const SIZE = 25;

@Component({
  selector: 'app-search-filter',
  templateUrl: './search-filter.component.html',
  styleUrls: ['./search-filter.component.css']
})
export class SearchFilterComponent implements OnInit{
  transportBrands: TransportBrand[];
  transportModels: TransportModel[];
  generationsTransport: GenerationTransport[];
  transportParameters: TransportParameters[];

  groupGenerationsTransport: Array<Array<GenerationTransport>>;

  isTransportBrandsLoaded = false;
  isTransportModelsLoaded = false;
  isGenerationsTransportLoaded = false;
  isTransportParametersLoaded = false;

  constructor(private transportService: TransportService, private imageService: ImageUploadService) {
    // @ts-ignore
    this.transportBrands = null;
    // @ts-ignore
    this.transportModels = null;
    // @ts-ignore
    this.generationsTransport = null;
    // @ts-ignore
    this.groupGenerationsTransport = null;
    // @ts-ignore
    this.transportParameters = null;
  }

  makeIsTransportModelsLoadedFalse() {
    this.makeIsGenerationsTransportLoadedFalse();
    this.isTransportModelsLoaded = false;
    // @ts-ignore
    this.transportModels = null;
  }

  makeIsGenerationsTransportLoadedFalse() {
    this.makeIsTransportParametersLoadedFalse();
    this.isGenerationsTransportLoaded = false;
    // @ts-ignore
    this.generationsTransport = null;
    // @ts-ignore
    this.groupGenerationsTransport = null;
  }

  makeIsTransportParametersLoadedFalse() {
    this.isTransportParametersLoaded = false;
    // @ts-ignore
    this.transportParameters = null;
  }

  findTransportModelsByTransportBrandId(modelId: number){
    this.transportService.getTransportModelsByTransportBrandId(PAGE, SIZE, modelId).subscribe(value => {
      this.transportModels = value.content;
      console.log(this.transportModels);
      this.isTransportModelsLoaded = true;
      this.makeIsGenerationsTransportLoadedFalse();
    })
  }

  findGenerationsTransportByTransportModelId(transportModelId: number) {
    this.transportService.getGenerationsTransportByTransportModelId(transportModelId)
      .subscribe(value => {
        this.generationsTransport = value;
        console.log(this.generationsTransport);
        this.generationsTransport.forEach(generationTransport => {
          this.imageService.getGenerationTransportImage(generationTransport.id).subscribe(file => {
            generationTransport.image = file.imageBytes;
          });
        })
        this.groupTransport(this.generationsTransport);
        this.isGenerationsTransportLoaded = true;
      }, error => {
        console.log(error);
        this.makeIsGenerationsTransportLoadedFalse();
      })
  }

  findTransportParametersByGenerationTransportId(generationTransportId: number) {
    this.transportService.getTransportParametersByGenerationTransportId(generationTransportId)
      .subscribe(value => {
        console.log(value);
        this.transportParameters = value;
        console.log("transport parameters");
        console.log(this.transportParameters);
        this.isTransportParametersLoaded = true;
      }, error => {
        console.log(error);
        this.makeIsTransportParametersLoadedFalse();
      })
  }

  ngOnInit(): void {
    this.transportService.getTransportBrands(PAGE, SIZE)
      .subscribe(value => {
        this.transportBrands = value.content;
        this.isTransportBrandsLoaded = true;
        console.log(this.transportBrands);
      })
  }

  formatImage(img: any): any {
    if (img == null){
      return null;
    }
    return 'data:image/jpeg;base64,' + img;
  }

  private groupTransport(generationsTransport: GenerationTransport[]): void {
    let column = 3;
    let row = generationsTransport.length % column == 0 ? generationsTransport.length / column : parseInt(String(generationsTransport.length / column)) + 1;

    console.log(row + ' row');
    console.log(column + ' column');

    this.groupGenerationsTransport = new Array<Array<GenerationTransport>>(row);
    for (let i = 0; i < row; i++) {
      this.groupGenerationsTransport[i] = new Array<GenerationTransport>();
    }

    for (let i = 0, currentIndex = 0; i < row; i++) {
      for (let j = 0; j < column  && currentIndex < generationsTransport.length; j++){
        this.groupGenerationsTransport[i][j] = generationsTransport[currentIndex++];
      }
    }
  }
}
