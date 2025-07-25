import ImagePlaceholder from '@/components/common/ImagePlaceholder.tsx'

export default function ImageCarousel() {
  const images = new Array(6).fill(1)

  return (
    <div>
      <div className="carousel w-full">
        {images.map((_, i) => (
          <div
            id={`image${i}`}
            className="carousel-item w-full aspect-[4/3] rounded-box overflow-hidden"
          >
            <ImagePlaceholder />
          </div>
        ))}
      </div>

      <div className="flex w-full justify-center gap-2 py-2">
        {images.map((_, i) => (
          <a href={`#image${i}`} className="rounded-box overflow-hidden">
            <ImagePlaceholder />
          </a>
        ))}
      </div>
    </div>
  )
}
